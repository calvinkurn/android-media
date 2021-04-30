package com.tokopedia.sellerfeedback.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.sellerfeedback.R
import com.tokopedia.sellerfeedback.presentation.adapter.FeedbackPageAdapter
import com.tokopedia.sellerfeedback.presentation.adapter.FeedbackPageAdapterFactory
import com.tokopedia.sellerfeedback.presentation.uimodel.FeedbackPageUiModel
import com.tokopedia.sellerfeedback.presentation.viewholder.FeedbackPageViewHolder
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_seller_feedback_page_chooser.*

class SellerFeedbackPageChooserBottomSheet(private val selectedTitle: String) : BottomSheetUnify(), FeedbackPageViewHolder.FeedbackPageListener {

    companion object {
        @LayoutRes
        private val LAYOUT = R.layout.bottom_sheet_seller_feedback_page_chooser
        private val TAG: String? = SellerFeedbackPageChooserBottomSheet::class.java.canonicalName

        fun createInstance(selectedTitle: String): SellerFeedbackPageChooserBottomSheet {
            return SellerFeedbackPageChooserBottomSheet(selectedTitle).apply {
                clearContentPadding = true
                showCloseIcon = false
                showKnob = true
                isKeyboardOverlap = false
            }
        }
    }

    private val adapter by lazy { FeedbackPageAdapter(FeedbackPageAdapterFactory(this)) }
    private var listener: BottomSheetListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        inflateLayout(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, TAG)
    }

    private fun dismiss(manager: FragmentManager) {
        (manager.findFragmentByTag(TAG) as? BottomSheetUnify)
                ?.dismissAllowingStateLoss()
    }

    fun setListener(listener: BottomSheetListener) {
        this.listener = listener
    }

    private fun initView() {
        with(rvFeedbackPage) {
            adapter = this@SellerFeedbackPageChooserBottomSheet.adapter
            layoutManager = LinearLayoutManager(context)
        }
        adapter.addElement(FeedbackPageUiModel(selectedTitle))
        adapter.notifyDataSetChanged()
    }

    private fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?) {
        val itemView = inflater.inflate(LAYOUT, container)
        val menuTitle = itemView.context.getString(R.string.seller_feedback_choose_page)
        setTitle(menuTitle)
        setChild(itemView)
    }

    override fun onItemClicked(title: String) {
        activity?.supportFragmentManager?.let {
            listener?.onPageSelected(title)
            dismiss(it)
        }
    }

    fun interface BottomSheetListener {
        fun onPageSelected(title: String)
    }
}