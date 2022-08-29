package com.tokopedia.data_explorer.db_explorer.presentation.schema

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.data_explorer.R
import com.tokopedia.data_explorer.db_explorer.di.DataExplorerComponent
import com.tokopedia.data_explorer.db_explorer.presentation.Constants
import com.tokopedia.data_explorer.db_explorer.presentation.Searchable
import kotlinx.android.synthetic.main.fragment_database_list_layout.*
import javax.inject.Inject

class SchemaFragment: BaseDaggerFragment(), Searchable {

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
                putExtra(Constants.Keys.DATABASE_NAME, databaseName)
                putExtra(Constants.Keys.DATABASE_PATH, databasePath)
                putExtra(Constants.Keys.SCHEMA_NAME, schema)
            }
        )
    }

    private val databaseName: String by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getString(Constants.Keys.DATABASE_NAME).orEmpty()
    }

    private val databasePath: String by lazy(LazyThreadSafetyMode.NONE) {
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
        setUpSchemaSearch()
        rvDatabaseList.adapter = schemaAdapter
        rvDatabaseList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setUpSchemaSearch() {
        searchInputView.searchBarPlaceholder = "Enter Schema Name"
        searchInputView.searchBarTextField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.toString()?.apply { search(this) } ?: run { search(null)}
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        searchInputView.clearListener = { search(null) }

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
    override fun search(query: String?)  = viewModel.getTables(databasePath, query)
    override fun searchQuery() = searchInputView.searchBarTextField.text.toString()

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