package com.tokopedia.otp.verification.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
            itemView.method_text.text = MethodChecker.fromHtml(modeList.otpListText)
            itemView.setOnClickListener {
                listener?.onModeListClick(modeList, position)
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