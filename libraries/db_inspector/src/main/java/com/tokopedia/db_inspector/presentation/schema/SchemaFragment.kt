package com.tokopedia.db_inspector.presentation.schema

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.db_inspector.R
import com.tokopedia.db_inspector.di.DbInspectorComponent
import com.tokopedia.db_inspector.presentation.Constants
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_database_list_layout.*
import javax.inject.Inject

class SchemaFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val viewModel: SchemaViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(SchemaViewModel::class.java)
    }

    private val schemaAdapter: SchemaAdapter by lazy {
        SchemaAdapter(
            onClick = {
                Toaster.build(rvDatabaseList, it, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
            }
        )
    }

    private val databaseName: String by lazy {
        arguments?.getString(Constants.Keys.DATABASE_NAME).orEmpty()
    }

    private val databasePath: String by lazy {
        arguments?.getString(Constants.Keys.DATABASE_PATH).orEmpty()
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
    override fun initInjector() = getComponent(DbInspectorComponent::class.java).inject(this)

    companion object {
        fun newInstance(databaseName: String, databasePath: String) : SchemaFragment {
            val fragment = SchemaFragment()
            val bundle = Bundle().apply {
                putString(Constants.Keys.DATABASE_NAME, databaseName)
                putString(Constants.Keys.DATABASE_PATH, databasePath)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}