package com.tokopedia.shop.flash_sale.presentation.draft.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flash_sale.presentation.draft.adapter.DraftListAdapter
import com.tokopedia.shop.flash_sale.presentation.draft.uimodel.DraftItemModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.date.addTimeToSpesificDate
import java.util.*

class DraftListBottomSheet: BottomSheetUnify() {

    companion object {
        const val TAG = "Tag New User Specification Bottom Sheet"
    }

    init {
        setTitle("Draft campaign penuh")
        setCloseClickListener {
            dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun initChildLayout() {
        overlayClickDismiss = true
        val contentView: View? = View.inflate(context, R.layout.ssfs_bottomsheet_draft_list , null)
        val rvDraft: RecyclerView? = contentView?.findViewById(R.id.rvDraft)
        setChild(contentView)

        rvDraft?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvDraft?.adapter = DraftListAdapter().apply {
            setItems(listOf(
                DraftItemModel("1", "title 1", "desc1",
                    Date(),
                    Date().addTimeToSpesificDate(Calendar.YEAR, 1)
                        .addTimeToSpesificDate(Calendar.HOUR, 2)
                ),
                DraftItemModel("2", "title 2", "desc2", Date(), Date()),
            ))
            setDeleteIconClickListener(::onDeleteIconClick)
        }

    }

    private fun onDeleteIconClick(draftItemModel: DraftItemModel) {
        println(draftItemModel.toString())
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this , TAG)
        }
    }
}