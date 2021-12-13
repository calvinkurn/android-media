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
import com.tokopedia.data_explorer.db_explorer.di.DataExplorerComponent
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.DataBaseController
import com.tokopedia.data_explorer.db_explorer.presentation.Constants
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.data_explorer_fragment_content_layout.*
import kotlinx.android.synthetic.main.data_explorer_pager_layout.*
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
        if (dataBaseController.hasSchemaData)
            viewModel.getTableInfo(databasePath, schemaName)
        else showDatabaseError(GlobalError.PAGE_NOT_FOUND)
        observeViewModels()
        initListeners()
        contentAdapter.onClick = { cell ->
            cell.text?.let { cellText ->
                ContentPreviewBottomSheet.show(
                    cellText,
                    childFragmentManager
                )
            }
        }
    }

    private fun initListeners() {
        pageNumberEditor.apply {
            setValue(1)
            stepValue = 1
            minValue = 1
            editText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    queryContent(getValue())
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            setAddClickListener { queryContent(getValue()) }
            setSubstractListener { queryContent(getValue()) }
            setValueChangedListener { newValue, _, _ ->
                queryContent(newValue)
            }
        }
    }

    private fun queryContent(page: Int = 1) {
        viewModel.getTableContent(databasePath, schemaName, page)
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
            viewModel.getTableRowsCount(databasePath, schemaName)
        })
        viewModel.resultRowLiveData.observe(viewLifecycleOwner, {
            if (it) queryContent() else showDatabaseError(GlobalError.MAINTENANCE)
        })
        viewModel.contentLiveData.observe(viewLifecycleOwner, { cells ->
            rvContent.adapter = contentAdapter
            contentAdapter.submitList(cells)
        })
        viewModel.errorLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is IllegalStateException -> Toaster.build(
                    rvContent,
                    "Incorrect Request",
                    Toaster.LENGTH_SHORT,
                    Toaster.TYPE_ERROR
                ).show()
                else -> showDatabaseError(GlobalError.MAINTENANCE)

            }
        })
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