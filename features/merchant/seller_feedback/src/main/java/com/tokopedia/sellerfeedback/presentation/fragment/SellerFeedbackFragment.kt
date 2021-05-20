package com.tokopedia.sellerfeedback.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.sellerfeedback.R
import com.tokopedia.sellerfeedback.presentation.adapter.SellerFeedbackAdapter
import com.tokopedia.sellerfeedback.presentation.adapter.SellerFeedbackAdapterFactory
import com.tokopedia.sellerfeedback.presentation.uimodel.SellerFeedbackFormUiModel
import com.tokopedia.sellerfeedback.presentation.view.SellerFeedbackToolbar
import com.tokopedia.sellerfeedback.presentation.viewholder.SellerFeedbackFormViewHolder.SellerFeedbackFormListener
import kotlinx.android.synthetic.main.fragment_seller_feedback.*
import kotlinx.android.synthetic.main.item_seller_feedback_form.*

class SellerFeedbackFragment : Fragment(), SellerFeedbackFormListener {

    private val toolbar by lazy { SellerFeedbackToolbar(requireActivity()) }
    private val adapter by lazy { SellerFeedbackAdapter(SellerFeedbackAdapterFactory(this)) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_seller_feedback, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(rvFeedbackForm) {
            adapter = this@SellerFeedbackFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }

        adapter.addElement(SellerFeedbackFormUiModel)
        adapter.notifyDataSetChanged()
    }

    override fun onClickFeedbackBtn(@ColorRes colorId: Int) {
        val color = ContextCompat.getColor(requireContext(), colorId)
        backgroundHeader.setBackgroundColor(color)
        toolbar.setupBackground(colorId)
    }
}