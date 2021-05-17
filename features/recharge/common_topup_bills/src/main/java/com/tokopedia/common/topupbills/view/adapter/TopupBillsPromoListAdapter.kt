package com.tokopedia.common.topupbills.view.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.data.TopupBillsPromo
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by nabillasabbaha on 23/04/19.
 */
class TopupBillsPromoListAdapter(val digitalPromoList: List<TopupBillsPromo>) :
        RecyclerView.Adapter<TopupBillsPromoListAdapter.PromoItemViewHolder>() {

    private var listener: ActionListener? = null

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    fun resetPromoListSelected(promoId: Int) {
        for (i in digitalPromoList.indices) {
            if (digitalPromoList[i].voucherCodeCopied && digitalPromoList[i].id != promoId) {
                digitalPromoList[i].voucherCodeCopied = false
                notifyItemChanged(i)
                break
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromoItemViewHolder {
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
        private val btnCopyPromo: UnifyButton = itemView.findViewById(R.id.btn_copy_promo)
        private val promoCodeLayout: RelativeLayout = itemView.findViewById(R.id.layout_promo_code)

        private lateinit var topupBillsPromo: TopupBillsPromo

        init {
            container.setOnClickListener {
                listener?.onClickPromoItem(topupBillsPromo, adapterPosition)
            }

            btnCopyPromo.setOnClickListener {
                for (i in digitalPromoList.indices) {
                    if (digitalPromoList[i].voucherCodeCopied) {
                        digitalPromoList[i].voucherCodeCopied = false
                        notifyItemChanged(i)
                        break
                    }
                }

                topupBillsPromo.voucherCodeCopied = true
                notifyItemChanged(adapterPosition)
                listener?.onClickPromoCode(topupBillsPromo.id, topupBillsPromo.promoCode)
            }
        }

        fun bind() {
            topupBillsPromo = digitalPromoList[adapterPosition]
            description.text = topupBillsPromo.title
            promoCode.text = topupBillsPromo.promoCode

            var containerBg = AppCompatResources.getDrawable(itemView.context, R.drawable.digital_bg_transparent_border_green)
            if (topupBillsPromo.voucherCodeCopied) {
                btnCopyPromo.text = itemView.context.getString(R.string.common_topup_text_has_copied_promo_code)
                btnCopyPromo.buttonVariant = UnifyButton.Variant.FILLED
                btnCopyPromo.buttonType = UnifyButton.Type.MAIN
            } else {
                containerBg = AppCompatResources.getDrawable(itemView.context, R.drawable.common_topup_bg_transparent_round)
                btnCopyPromo.text = itemView.context.getString(R.string.common_topup_text_copy_promo_code)
                btnCopyPromo.buttonVariant = UnifyButton.Variant.GHOST
                btnCopyPromo.buttonType = UnifyButton.Type.MAIN
            }
            container.background = containerBg

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