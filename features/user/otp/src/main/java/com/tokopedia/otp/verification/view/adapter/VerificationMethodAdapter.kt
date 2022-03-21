package com.tokopedia.otp.verification.view.adapter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.otp.databinding.ItemVerificationMethodBinding
import com.tokopedia.otp.verification.domain.pojo.ModeListData
import com.tokopedia.unifyprinciples.R as RUnify

/**
 * @author rival
 * @created on 9/12/2019
 */

open class VerificationMethodAdapter(
        var listData: MutableList<ModeListData>,
        private val listener: ClickListener
) : RecyclerView.Adapter<VerificationMethodAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding = ItemVerificationMethodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    fun setList(listData: MutableList<ModeListData>) {
        this.listData.clear()
        this.listData.addAll(listData)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listData[position], listener, position)
    }

    class ViewHolder(val binding: ItemVerificationMethodBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(modeList: ModeListData, listener: ClickListener?, position: Int) {
            with(binding) {
                methodIcon.setImageUrl(modeList.otpListImgUrl)
                methodIcon.scaleType = ImageView.ScaleType.FIT_CENTER
                binding.container.setOnClickListener {
                    listener?.onModeListClick(modeList, position)
                }

                val otpListTextHtml = MethodChecker.fromHtml(modeList.otpListText)
                val indexNewline = otpListTextHtml.indexOf("\n")
                val clickableSpan = object : ClickableSpan() {
                    override fun onClick(widget: View) {}

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = MethodChecker.getColor(methodText.context, RUnify.color.Unify_N700)
                        ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    }
                }

                val spannable: Spannable
                spannable = SpannableString(otpListTextHtml)
                if(indexNewline > -1) {
                    spannable.setSpan(clickableSpan, 0, indexNewline, 0)
                }
                methodText.setText(spannable, TextView.BufferType.SPANNABLE)
            }
        }
    }

    interface ClickListener {
        fun onModeListClick(modeList: ModeListData, position: Int)
    }

    companion object {
        
        fun createInstance(listener: ClickListener): VerificationMethodAdapter {
            return VerificationMethodAdapter(mutableListOf(), listener)
        }
    }
}