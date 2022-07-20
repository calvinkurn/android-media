package com.tokopedia.data_explorer.db_explorer.presentation.content.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.data_explorer.R
import com.tokopedia.data_explorer.db_explorer.di.DataExplorerComponent
import com.tokopedia.data_explorer.db_explorer.presentation.content.ContentViewModel
import kotlinx.android.synthetic.main.data_explorer_column_list_bottomsheet.*
import kotlinx.android.synthetic.main.data_explorer_column_list_bottomsheet.view.*
import javax.inject.Inject

class ColumnPickerBottomSheet : BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: ContentViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(requireActivity(), viewModelFactory)
        viewModelProvider.get(ContentViewModel::class.java)
    }

    private val query by lazy {
        arguments?.getString("query") ?: ""
    }

    private lateinit var component: DataExplorerComponent
    private lateinit var adapter: ColumnSelectorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
    }

    private fun inject() {
        component =
            DataExplorerComponent::class.java.cast((activity as (HasComponent<DataExplorerComponent>)).component)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.data_explorer_column_list_bottomsheet, container, false)

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val columnList = ArrayList(getColumnList())
        adapter = ColumnSelectorAdapter(columnList)
        rvColumnList.adapter = adapter
        rvColumnList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        searchBtn.setOnClickListener {

            if (adapter.lastCheckedColumnIndex >= 0) {
                viewModel.search(query, columnList.getOrNull(adapter.lastCheckedColumnIndex) ?: "")
            }
            else Toast.makeText(context, "Please select one column", Toast.LENGTH_LONG).show()
        }
    }

    private fun getColumnList(): List<String> {
        return viewModel.columnHeaderList.filterNot {
            it.text.isNullOrEmpty()
        }.map { it.text ?: "" }
    }


    companion object {
        const val TAG = "ColumnPickerBottomSheet"
        fun showBottomSheet(searchQuery: String, childFragmentManager: FragmentManager) {
            val sheet = ColumnPickerBottomSheet()
            sheet.arguments = Bundle().apply { putString("query", searchQuery) }
            sheet.show(childFragmentManager, TAG)
        }
    }
}