package com.tokopedia.data_explorer.db_explorer.presentation.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.data_explorer.R
import com.tokopedia.data_explorer.db_explorer.data.models.cursor.input.Order
import com.tokopedia.data_explorer.db_explorer.di.DataExplorerComponent
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.Cell
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.DataBaseController
import com.tokopedia.data_explorer.db_explorer.extensions.InvalidPageRequestException
import com.tokopedia.data_explorer.db_explorer.extensions.setupGrid
import com.tokopedia.data_explorer.db_explorer.presentation.Constants
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.data_explorer_fragment_content_layout.*
import javax.inject.Inject

class ContentFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val contentAdapter: ContentAdapter = ContentAdapter()

    private val viewModel: ContentViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(ContentViewModel::class.java)
    }
    private val databaseName: String by lazy(LazyThreadSafetyMode.NONE) {
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
        if (dataBaseController.hasSchemaData) {
            viewModel.databasePath = databasePath
            viewModel.getTableInfo(schemaName)

        } else showDatabaseError("No Schema Available")
        observeViewModels()
        initListeners()
    }

    private fun initListeners() {
        contentAdapter.onClick = { cell -> onCellClicked(cell)}
        contentAdapter.headerItemClick = { header ->
            queryContent(header.text, header.order)
            viewModel.updateHeader(header)
        }

        pageNumberEditor.apply {
            setValue(1)
            stepValue = 1
            minValue = 1
            editText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.currentPage = getValue()
                    queryContent()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            setAddClickListener {
                viewModel.currentPage = getValue()
                queryContent()
            }
            setSubstractListener {
                viewModel.currentPage = getValue()
                queryContent()
            }
        }
    }

    private fun queryContent(orderBy: String? = null, sort: Order = Order.ASCENDING) {
        viewModel.getTableContent(schemaName, orderBy, sort)
    }

    private fun showDatabaseError(errorType: String) {
        tableGroup.gone()
        dataExplorerEmptyState.visible()
        dataExplorerEmptyState.setDescription(errorType)
        dataExplorerEmptyState.setImageUrl("https://ecs7.tokopedia.net/android/shop_page/image_product_empty_state_buyer.png")
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
            viewModel.getTableRowsCount(schemaName)
        })
        viewModel.resultRowLiveData.observe(viewLifecycleOwner, {
            if (it) queryContent() else showDatabaseError("Table has no content to show")
        })
        viewModel.contentLiveData.observe(viewLifecycleOwner, { cells ->
            dataExplorerEmptyState.gone()
            rvContent.adapter = contentAdapter
            contentAdapter.submitList(cells)
        })
        viewModel.errorLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is InvalidPageRequestException -> {
                    pageNumberEditor.setValue(viewModel.currentPage)
                    showToast(it.message, Toaster.TYPE_NORMAL)

                }
                is IllegalStateException-> showToast(it.message, Toaster.TYPE_ERROR)
                else -> showDatabaseError(it.message?:"Invalid Request")

            }
        })
    }

    private fun onCellClicked(cell: Cell) {
        cell.text?.let { cellText ->
            ContentPreviewBottomSheet.show(
                cellText,
                childFragmentManager
            )
        }
    }

    fun clearContents() {
        viewModel.dropTable(schemaName)
    }

    private fun showToast(message: String?, type: Int) {
        Toaster.build(
            rvContent,
            message ?: "Incorrect Request",
            Toaster.LENGTH_SHORT,
            type
        ).show()
    }

    override fun getScreenName() = ""
    override fun initInjector() = getComponent(DataExplorerComponent::class.java).inject(this)

    companion object {
        fun newInstance(
            schemaName: String,
            databaseName: String,
            databasePath: String
        ): ContentFragment {
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