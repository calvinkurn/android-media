package com.tokopedia.data_explorer.presentation.content

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.data_explorer.R
import com.tokopedia.data_explorer.di.DataExplorerComponent
import com.tokopedia.data_explorer.extensions.setupGrid
import com.tokopedia.data_explorer.presentation.Constants
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.data_explorer_fragment_content_layout.*
import kotlinx.android.synthetic.main.fragment_database_list_layout.*
import javax.inject.Inject

class ContentFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val headerAdapter: HeaderAdapter = HeaderAdapter()
    private val contentAdapter: ContentAdapter = ContentAdapter()

    private val viewModel: ContentViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(ContentViewModel::class.java)
    }

    private val databaseName: String by lazy(LazyThreadSafetyMode.NONE){
        arguments?.getString(Constants.Keys.DATABASE_NAME).orEmpty()
    }

    private val databasePath: String by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getString(Constants.Keys.DATABASE_PATH).orEmpty()
    }

    private val schemaName: String by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getString(Constants.Keys.SCHEMA_NAME).orEmpty()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.data_explorer_fragment_content_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTableInfo(databasePath, schemaName)
        observeViewModels()
        headerAdapter.onClick = {

        }
        contentAdapter.onClick = {

        }
    }

    private fun observeViewModels() {
        viewModel.columnHeaderLiveData.observe(viewLifecycleOwner, { cells ->
            headerAdapter.setItems(cells)
            contentAdapter.headersCount = cells.size
            rvContent.setupGrid()
            rvContent.layoutManager = GridLayoutManager(
                context,
                cells.size,
                RecyclerView.VERTICAL,
                false
            )
            //rvContent.adapter = headerAdapter

            viewModel.getTableContent(databasePath, schemaName)
            Log.d("DATAEXPLORER 1", cells.map { it.text }.joinToString())
        })
        viewModel.contentLiveData.observe(viewLifecycleOwner, { cells ->
            rvContent.adapter = contentAdapter

            contentAdapter.submitList(cells)
            Log.d("DATAEXPLORER 2", cells.map { it.text }.joinToString())
        })
        viewModel.errorLiveData.observe(viewLifecycleOwner, {
            Toaster.build(rvDatabaseList, "Error", Toaster.TYPE_NORMAL, Toaster.LENGTH_LONG).show()
        })
    }


    override fun getScreenName() = ""
    override fun initInjector() = getComponent(DataExplorerComponent::class.java).inject(this)

    companion object {
        fun newInstance(schemaName: String, databaseName: String, databasePath: String) : ContentFragment {
            val fragment = ContentFragment()
            val bundle = Bundle().apply {
                putString(Constants.Keys.DATABASE_NAME, databaseName)
                putString(Constants.Keys.DATABASE_PATH, databasePath)
                putString(Constants.Keys.SCHEMA_NAME, schemaName)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}