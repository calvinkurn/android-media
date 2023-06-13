package com.tokopedia.entertainment.pdp.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.entertainment.databinding.BottomSheetEventRedeemRevampBinding
import com.tokopedia.entertainment.pdp.adapter.BaseEventRedeemRevampAdapter
import com.tokopedia.entertainment.pdp.adapter.EventRedeemRevampAdapter
import com.tokopedia.entertainment.pdp.adapter.diffutil.EventRedeemRevampDiffer
import com.tokopedia.entertainment.pdp.adapter.factory.EventRedeemRevampAdapterTypeFactory
import com.tokopedia.entertainment.pdp.adapter.viewholder.EventParticipantTitleViewHolder
import com.tokopedia.entertainment.pdp.adapter.viewholder.EventParticipantViewHolder
import com.tokopedia.entertainment.pdp.uimodel.ParticipantTitleUiModel
import com.tokopedia.entertainment.pdp.uimodel.ParticipantUiModel
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.entertainment.R.string as stringRedeem

/**
 * Author firmanda on 17,Nov,2022
 */

class EventRedeemRevampBottomSheet: BottomSheetUnify(),
    EventParticipantViewHolder.ParticipantListener,
    EventParticipantTitleViewHolder.ParticipantTitleListener,
    BaseEventRedeemRevampAdapter.EventRedeemBaseAdapterListener
{

    private var binding by autoClearedNullable<BottomSheetEventRedeemRevampBinding>()
    private val adapter by lazy(LazyThreadSafetyMode.NONE){
        EventRedeemRevampAdapter(
            typeFactory = EventRedeemRevampAdapterTypeFactory(this, this),
            differ = EventRedeemRevampDiffer(),
            this
        )
    }
    private var listener: RedeemBottomSheetListener? = null
    private var listParticipant: List<Visitable<*>> = mutableListOf()
    private var listCheckedIds: MutableList<Pair<String, Boolean>> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initView() {
        context?.let { context ->
            isFullpage = false
            isDragable = false
            showCloseIcon = true
            clearContentPadding = true

            binding = BottomSheetEventRedeemRevampBinding.inflate(LayoutInflater.from(context))

            initRV()
            setTitle(context.resources.getString(stringRedeem.ent_redeem_revamp_bottomsheet_title))
            setChild(binding?.root)
            setAction(context.resources.getString(stringRedeem.ent_redeem_revamp_bottomsheet_save)
            ) {
                saveList()
            }
        }
    }

    private fun initRV() {
        context?.let { context ->
            binding?.rvParticipantList?.show()

            val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            binding?.rvParticipantList?.adapter = this@EventRedeemRevampBottomSheet.adapter
            binding?.rvParticipantList?.layoutManager = layoutManager
            showLists(listParticipant)
        }
    }

    private fun showLists(items: List<Visitable<*>>) {
        adapter.submitList(items)
    }

    private fun saveList() {
        listener?.onClickSave(listCheckedIds)
        dismiss()
    }

    fun setList(items: List<Visitable<*>>) {
        this.listParticipant = items
    }

    fun setListener(listener: RedeemBottomSheetListener) {
        this.listener = listener
    }

    override fun onCheckListener(element: ParticipantUiModel, isChecked: Boolean, position: Int) {
        adapter.updateOneItem(isChecked, position)
    }

    override fun onCheckTitleListener(element: ParticipantTitleUiModel, isChecked: Boolean, position: Int) {
        adapter.updateAllListItem(isChecked, position)
    }

    override fun updateListChecked(listCheckedIds: MutableList<Pair<String, Boolean>>) {
        this.listCheckedIds = listCheckedIds
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener?.onClose()
    }

    companion object {
        fun getInstance(): EventRedeemRevampBottomSheet = EventRedeemRevampBottomSheet()
    }

    interface RedeemBottomSheetListener{
        fun onClickSave(listCheckedIds: List<Pair<String, Boolean>>)
        fun onClose()
    }
}
