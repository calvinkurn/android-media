package com.tokopedia.topupbills.telco.view.adapter

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.getColorFromResources
import com.tokopedia.common.topupbills.data.TopupBillsPromo

/**
 * Created by nabillasabbaha on 23/04/19.
 */
class DigitalPromoListAdapter(val digitalPromoList: List<TopupBillsPromo>) :
        RecyclerView.Adapter<DigitalPromoListAdapter.PromoItemViewHolder>() {

    private lateinit var context: Context
    private lateinit var listener: ActionListener

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    fun resetPromoListSelected(promoId: Int) {
        for (i in 0 until digitalPromoList.size) {
            if (digitalPromoList[i].voucherCodeCopied && digitalPromoList[i].id != promoId) {
                digitalPromoList[i].voucherCodeCopied = false
                notifyItemChanged(i)
                break
            }
        }
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

        private val container: LinearLayout = itemView.findViewById(R.id.promo_container)
        private val description: TextView = itemView.findViewById(R.id.desc_promo)
        private val promoCode: TextView = itemView.findViewById(R.id.promo_code)
        private val btnCopyPromo: TextView = itemView.findViewById(R.id.btn_copy_promo)
        private val promoCodeLayout: RelativeLayout = itemView.findViewById(R.id.layout_promo_code)

        private lateinit var topupBillsPromo: TopupBillsPromo

        init {
            container.setOnClickListener {
                listener.onClickPromoItem(topupBillsPromo, adapterPosition)
            }

            btnCopyPromo.setOnClickListener {
                for (i in 0 until digitalPromoList.size) {
                    if (digitalPromoList[i].voucherCodeCopied) {
                        digitalPromoList[i].voucherCodeCopied = false
                        notifyItemChanged(i)
                        break
                    }
                }

                topupBillsPromo.voucherCodeCopied = true
                notifyItemChanged(adapterPosition)
                listener.onClickPromoCode(topupBillsPromo.id, topupBillsPromo.promoCode)
            }
        }

        fun bind() {
            topupBillsPromo = digitalPromoList[adapterPosition]
            description.text = topupBillsPromo.title
            promoCode.text = topupBillsPromo.promoCode

            var containerBg = AppCompatResources.getDrawable(context, R.drawable.digital_bg_transparent_border_green)
            var btnCopyBg = AppCompatResources.getDrawable(context, R.drawable.digital_bg_green_rounded)
            if (topupBillsPromo.voucherCodeCopied) {
                btnCopyPromo.text = context.getString(R.string.text_has_copied_promo_code)
                btnCopyPromo.setTextColor(context.resources.getColorFromResources(context, com.tokopedia.design.R.color.white))
            } else {
                containerBg = AppCompatResources.getDrawable(context, R.drawable.digital_bg_transparent_round)
                btnCopyBg = AppCompatResources.getDrawable(context, R.drawable.digital_bg_transparent_border_green)
                btnCopyPromo.text = context.getString(R.string.text_copy_promo_code)
                btnCopyPromo.setTextColor(context.resources.getColorFromResources(context, com.tokopedia.design.R.color.tkpd_main_green))
            }
            container.background = containerBg
            btnCopyPromo.background = btnCopyBg

            if (TextUtils.isEmpty(topupBillsPromo.promoCode)) {
                promoCodeLayout.visibility = View.GONE
            } else {
                promoCodeLayout.visibility = View.VISIBLE
            }
        }

    }

    interface ActionListener {
        fun onClickPromoCode(promoId: Int, voucherCode: String)

        fun onClickPromoItem(topupBillsPromo: TopupBillsPromo, position: Int)
    }

}