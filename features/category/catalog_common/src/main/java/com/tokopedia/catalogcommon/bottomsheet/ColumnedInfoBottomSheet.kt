package com.tokopedia.catalogcommon.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.uimodel.ColumnedInfoUiModel
import com.tokopedia.catalogcommon.viewholder.columnedinfo.ColumnedInfoBottomSheetAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify

class ColumnedInfoBottomSheet: BottomSheetUnify() {

    companion object {
        const val TAG = "Tag ColumnedInfoBottomSheet"
        const val COLUMN_DATA_KEY = "column_data_key"
        const val TITLE_KEY = "title_key"

        fun show(
            manager: FragmentManager?,
            title: String = "",
            columnData: List<ColumnedInfoUiModel.ColumnData>
        ) {
            ColumnedInfoBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(TITLE_KEY, title)
                    putParcelableArrayList(COLUMN_DATA_KEY, ArrayList(columnData))
                }
            }.show(manager ?: return, TAG)
        }
    }

    private var rvColumnedInfo: RecyclerView? = null

    init {
        setCloseClickListener { dismiss() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val columnData: ArrayList<ColumnedInfoUiModel.ColumnData> =
            arguments?.getParcelableArrayList(COLUMN_DATA_KEY) ?: arrayListOf()
        val title: String = arguments?.getString(TITLE_KEY).orEmpty()

        setTitle(title)
        setupList(columnData)
    }

    private fun setupList(columnData: List<ColumnedInfoUiModel.ColumnData>) {
        rvColumnedInfo?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvColumnedInfo?.adapter = ColumnedInfoBottomSheetAdapter(columnData)
    }

    private fun initChildLayout() {
        overlayClickDismiss = true
        clearContentPadding = true
        val contentView: View? = View.inflate(context, R.layout.bottomsheet_columned_info , null)
        rvColumnedInfo = contentView?.findViewById(R.id.rvColumnedInfo)
        setChild(contentView)
    }
}
