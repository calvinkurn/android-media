package com.tokopedia.buyerorder.detail.view.adapter

import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import com.tokopedia.buyerorder.R

class RejectOrderReasonAdapter(var reasons: List<String>?) : RecyclerView.Adapter<RejectOrderReasonAdapter.ViewHolder>() {
    private var actionListener: ActionListener? = null
    private var currentSelected : Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_order_reject_reason,
                parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return reasons?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(reasons, actionListener, currentSelected == position)
    }


    fun setActionListener(actionListener: ActionListener) {
        this.actionListener = actionListener
    }

    fun markChecked(position: Int) {
        currentSelected = position
        notifyDataSetChanged()
    }

    interface ActionListener {
        fun onChecked(position: Int, reason: String?, reasonCode: Int)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var reasonTextView: TextView = view.findViewById(R.id.reason)
        private var reasonRadioButton: RadioButton = view.findViewById(R.id.radio_button)
        private var reasonLayout: LinearLayout = view.findViewById(R.id.reason_layout)
        private var divider: View = view.findViewById(R.id.divider)

        fun onBind(reason: List<String>?, actionListener: ActionListener?, isSelected: Boolean) {
            reasonTextView.text = reason?.get(adapterPosition)
            reasonLayout.setOnClickListener {
                reasonRadioButton.isChecked = true
            }
            if(adapterPosition == reason?.size?.minus(1)){
                divider.visibility = View.GONE
            } else {
                divider.visibility = View.VISIBLE
            }

            reasonRadioButton.isChecked = isSelected

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                reasonRadioButton.setButtonDrawable(com.tokopedia.design.R.drawable.selector_radiobutton_big)
            }

            reasonRadioButton.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    actionListener?.onChecked(adapterPosition, reason?.get(adapterPosition), 20+adapterPosition)
                }
            }
        }
    }
}