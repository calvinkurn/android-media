package com.tokopedia.data_explorer.db_explorer.presentation.schema

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.data_explorer.R
import com.tokopedia.data_explorer.db_explorer.di.DataExplorerComponent
import kotlinx.android.synthetic.main.fragment_database_list_layout.*
import javax.inject.Inject

class SchemaFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val viewModel: SchemaViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(SchemaViewModel::class.java)
    }

    private val schemaAdapter: SchemaAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SchemaAdapter(
            onClick = { schema ->
                navigateToContents(schema)
            }
        )
    }

    private fun navigateToContents(schema: String) {
        context?.startActivity(
            Intent(requireContext(), com.tokopedia.data_explorer.db_explorer.presentation.content.ContentActivity::class.java).apply {
                putExtra(com.tokopedia.data_explorer.db_explorer.presentation.Constants.Keys.DATABASE_NAME, databaseName)
                putExtra(com.tokopedia.data_explorer.db_explorer.presentation.Constants.Keys.DATABASE_PATH, databasePath)
                putExtra(com.tokopedia.data_explorer.db_explorer.presentation.Constants.Keys.SCHEMA_NAME, schema)
            }
        )
    }

    private val databaseName: String by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getString(com.tokopedia.data_explorer.db_explorer.presentation.Constants.Keys.DATABASE_NAME).orEmpty()
    }

    private val databasePath: String by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getString(com.tokopedia.data_explorer.db_explorer.presentation.Constants.Keys.DATABASE_PATH).orEmpty()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_database_list_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTables(databasePath)
        observeViewModels()
        rvDatabaseList.adapter = schemaAdapter
        rvDatabaseList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun observeViewModels() {
        viewModel.tableListLiveData.observe(viewLifecycleOwner, {
            schemaAdapter.submitList(it)
        })
        viewModel.errorLiveData.observe(viewLifecycleOwner, {
            schemaAdapter.submitList(emptyList())
        })
    }


    override fun getScreenName() = ""
    override fun initInjector() = getComponent(DataExplorerComponent::class.java).inject(this)

    companion object {
        fun newInstance(databaseName: String, databasePath: String) : SchemaFragment {
            val fragment = SchemaFragment()
            val bundle = Bundle().apply {
                putString(com.tokopedia.data_explorer.db_explorer.presentation.Constants.Keys.DATABASE_NAME, databaseName)
                putString(com.tokopedia.data_explorer.db_explorer.presentation.Constants.Keys.DATABASE_PATH, databasePath)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}