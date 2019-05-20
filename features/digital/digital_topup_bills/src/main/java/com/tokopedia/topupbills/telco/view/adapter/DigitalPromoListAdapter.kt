package com.tokopedia.digital.topupbillsproduct.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.getColorFromResources
import com.tokopedia.topupbills.telco.view.model.DigitalPromo

/**
 * Created by nabillasabbaha on 23/04/19.
 */
class DigitalPromoListAdapter(val digitalPromoList: List<DigitalPromo>) :
        RecyclerView.Adapter<DigitalPromoListAdapter.PromoItemViewHolder>() {

    private lateinit var context: Context
    private lateinit var listener: ActionListener

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromoItemViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_digital_telco_promo, parent, false)
        return PromoItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return digitalPromoList.size
    }

    override fun onBindViewHolder(holder: PromoItemViewHolder, position: Int) {
        holder.bind()
    }

    inner class PromoItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val container: RelativeLayout = itemView.findViewById(R.id.promo_container)
        private val description: TextView = itemView.findViewById(R.id.desc_promo)
        private val promoCode: TextView = itemView.findViewById(R.id.promo_code)
        private val btnCopyPromo: TextView = itemView.findViewById(R.id.btn_copy_promo)

        private lateinit var digitalPromo: DigitalPromo

        init {
            container.setOnClickListener {
                listener.onClickPromoItem(digitalPromo)
            }

            btnCopyPromo.setOnClickListener {
                for (i in 0..digitalPromoList.size-1) {
                    if (digitalPromoList.get(i).voucherCodeCopied) {
                        digitalPromoList.get(i).voucherCodeCopied = false
                        notifyItemChanged(i)
                        break
                    }
                }

                digitalPromo.voucherCodeCopied = true
                notifyItemChanged(adapterPosition)
                listener.onClickPromoCode(digitalPromo.promoCode)
            }
        }

        fun bind() {
            digitalPromo = digitalPromoList.get(adapterPosition)
            description.text = digitalPromo.description
            promoCode.text = digitalPromo.promoCode

            if (digitalPromo.voucherCodeCopied) {
                container.setBackgroundResource(R.drawable.digital_bg_transparent_border_green)
                btnCopyPromo.setBackgroundResource(R.drawable.digital_bg_green_rounded)
                btnCopyPromo.setText(context.getString(R.string.text_has_copied_promo_code))
                btnCopyPromo.setTextColor(context.resources.getColorFromResources(context, R.color.white))
            } else {
                container.setBackgroundResource(R.drawable.digital_bg_transparent_round)
                btnCopyPromo.setBackgroundResource(R.drawable.digital_bg_transparent_border_green)
                btnCopyPromo.setText(context.getString(R.string.text_copy_promo_code))
                btnCopyPromo.setTextColor(context.resources.getColorFromResources(context, R.color.tkpd_main_green))
            }
        }

    }

    interface ActionListener {
        fun onClickPromoCode(voucherCode: String)

        fun onClickPromoItem(digitalPromo: DigitalPromo)
    }

}