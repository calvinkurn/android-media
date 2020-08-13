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
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.otp.R
import com.tokopedia.otp.verification.domain.data.ModeListData
import kotlinx.android.synthetic.main.item_verification_method.view.*

/**
 * @author rival
 * @created on 9/12/2019
 */

class VerificationMethodAdapter(
        private var listData: MutableList<ModeListData>,
        private val listener: ClickListener
) : RecyclerView.Adapter<VerificationMethodAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_verification_method, parent, false)
        )
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

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(modeList: ModeListData, listener: ClickListener?, position: Int) {
            itemView.method_icon.setImageUrl(modeList.otpListImgUrl)
            itemView.method_icon.scaleType = ImageView.ScaleType.FIT_CENTER
            itemView.setOnClickListener {
                listener?.onModeListClick(modeList, position)
            }

            val spannable: Spannable
            val otpListTextHtml = MethodChecker.fromHtml(modeList.otpListText)
            spannable = SpannableString(otpListTextHtml)
            spannable.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {}

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = MethodChecker.getColor(itemView.context, R.color.Neutral_N700)
                    ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
            }, 0, otpListTextHtml.indexOf("\n"), 0)
            itemView.method_text.setText(spannable, TextView.BufferType.SPANNABLE)
        }
    }

    interface ClickListener {
        fun onModeListClick(modeList: ModeListData, position: Int)
    }

    companion object {

        val regexPattern = "\\u003c(\\/)?(b\\b)[^\\u003e]*\\u003e[\\s\\S]*\\u003c(\\/)?(b\\b)[^\\u003e]*\\u003e"
        
        fun createInstance(listener: ClickListener): VerificationMethodAdapter {
            return VerificationMethodAdapter(mutableListOf(), listener)
        }
    }
}