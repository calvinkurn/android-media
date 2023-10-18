package com.tokopedia.smartbills.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.data.SmartBillsCatalogMenu
import com.tokopedia.smartbills.presentation.adapter.SmartBillsAddBillsAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify

class SmartBillsCatalogBottomSheet: BottomSheetUnify(), SmartBillsAddBillsAdapter.SmartBillsCatalogsListener {

    init {
        isFullpage = false
        isDragable = false
        showCloseIcon = true
    }

    private val smartBillsAddBillsAdapter = SmartBillsAddBillsAdapter(this@SmartBillsCatalogBottomSheet)
    private lateinit var smartBillsCatalogRecycleView: RecyclerView
    private var listener: CatalogCallback? = null
    private var sbmCatalogMenu: ArrayList<SmartBillsCatalogMenu>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sbmCatalogMenu = it.getParcelableArrayList(CATALOG_MENU_EXTRA) ?: arrayListOf()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        smartBillsCatalogRecycleView = RecyclerView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        setChild(smartBillsCatalogRecycleView)
        setTitle(getString(R.string.smart_bills_add_bills_title_bottom_sheet_catalog))
        setCloseClickListener {
            listener?.onCloseCatalogBottomSheet()
            dismiss()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        smartBillsCatalogRecycleView.apply {
            adapter = smartBillsAddBillsAdapter
            layoutManager = GridLayoutManager(context, SMART_BILLS_CATALOG_SPAN)
            addItemDecoration(
                SpacingItemDecoration(getDimens(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3),
                        getDimens(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl5))
            )
            sbmCatalogMenu?.let { sbmCatalogMenu ->
                smartBillsAddBillsAdapter.listCatalogMenu = sbmCatalogMenu.toMutableList()
            }
        }
    }

    fun setListener(listener: CatalogCallback) {
        this.listener = listener
    }

    override fun onCatalogClicked(applink: String, category: String) {
        dismiss()
        listener?.onCatalogClickCallback(applink, category)
    }

    override fun dismiss() {
        super.dismiss()
    }

    companion object{
        private const val SMART_BILLS_CATALOG_SPAN = 5
        private const val CATALOG_MENU_EXTRA = "CATALOG_MENU_EXTRA"
        fun newInstance(sbmCatalogMenu: ArrayList<SmartBillsCatalogMenu>): SmartBillsCatalogBottomSheet {
            val bottomSheet = SmartBillsCatalogBottomSheet()
            val bundle = Bundle()
            bundle.putParcelableArrayList(CATALOG_MENU_EXTRA, sbmCatalogMenu)
            bottomSheet.arguments = bundle
            return bottomSheet
        }
    }

    interface CatalogCallback{
        fun onCatalogClickCallback(applink: String, category: String)
        fun onCloseCatalogBottomSheet()
    }
}
