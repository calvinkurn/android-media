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
import com.tokopedia.data_explorer.domain.shared.models.DataBaseController
import com.tokopedia.data_explorer.extensions.setupGrid
import com.tokopedia.data_explorer.presentation.Constants
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.visible
import kotlinx.android.synthetic.main.data_explorer_fragment_content_layout.*
import javax.inject.Inject

class ContentFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
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
    private val dataBaseController: DataBaseController by lazy(LazyThreadSafetyMode.NONE) {
        DataBaseController(databaseName, databasePath, schemaName)
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
        if (dataBaseController.hasSchemaData)
            viewModel.getTableInfo(databasePath, schemaName)
        else showDatabaseError(GlobalError.PAGE_NOT_FOUND)
        observeViewModels()
        contentAdapter.onClick = { cell ->
            cell.text?.let { cellText -> ContentPreviewBottomSheet.show(cellText, childFragmentManager) }
        }
    }

    private fun showDatabaseError(errorType: Int) {
        dataExplorerGlobalError.visible()
        dataExplorerGlobalError.setType(errorType)
    }

    private fun observeViewModels() {
        viewModel.columnHeaderLiveData.observe(viewLifecycleOwner, { cells ->
            contentAdapter.headersCount = cells.size
            rvContent.setupGrid()
            rvContent.layoutManager = GridLayoutManager(
                context,
                cells.size,
                RecyclerView.VERTICAL,
                false
            )
            viewModel.getTableContent(databasePath, schemaName)
            Log.d("DATAEXPLORER 1", cells.map { it.text }.joinToString())
        })
        viewModel.contentLiveData.observe(viewLifecycleOwner, { cells ->
            rvContent.adapter = contentAdapter
            contentAdapter.submitList(cells)
            Log.d("DATAEXPLORER 2", cells.map { it.text }.joinToString())
        })
        viewModel.errorLiveData.observe(viewLifecycleOwner, {
            showDatabaseError(GlobalError.MAINTENANCE)
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