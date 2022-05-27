package com.tokopedia.tokomember_seller_dashboard.view.viewholder

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmCouponActions
import com.tokopedia.tokomember_seller_dashboard.model.Actions
import com.tokopedia.tokomember_seller_dashboard.model.TripleDotsItem
import com.tokopedia.tokomember_seller_dashboard.model.VouchersItem
import com.tokopedia.tokomember_seller_dashboard.util.ADD_QUOTA
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_DELETED
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_ENDED
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_MEMBER
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_NOT_STARTED
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_ON_GOING
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_PROCESSING
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_STOPPED
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_VIP
import com.tokopedia.tokomember_seller_dashboard.util.DELETE
import com.tokopedia.tokomember_seller_dashboard.util.EDIT
import com.tokopedia.tokomember_seller_dashboard.util.STOP
import com.tokopedia.tokomember_seller_dashboard.view.adapter.mapper.ProgramUpdateMapper
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberOptionsMenuBottomsheet
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class TmCouponVh(itemView: View, val fragmentManager: FragmentManager) : RecyclerView.ViewHolder(itemView) {

    private lateinit var tvCouponState: Typography
    lateinit var tvDate: Typography
    lateinit var tvCouponTitle: Typography
    lateinit var tvMembership: Typography
    lateinit var tvQuota: Typography
    lateinit var viewStatus: View
    lateinit var optionMenu: IconUnify
    lateinit var ivCoupon: ImageUnify
    lateinit var btnAddQuota: UnifyButton

    @SuppressLint("ResourcePackage")
    fun bind(item: VouchersItem, tmCouponActions: TmCouponActions) {

        viewStatus = itemView.findViewById(R.id.view_status)
        tvCouponState = itemView.findViewById(R.id.tv_coupon_state)
        tvDate = itemView.findViewById(R.id.tv_date)
        tvCouponTitle = itemView.findViewById(R.id.tv_coupon_title)
        tvMembership = itemView.findViewById(R.id.tv_membership)
        tvQuota = itemView.findViewById(R.id.tv_quota)
        optionMenu = itemView.findViewById(R.id.icon_options)
        ivCoupon = itemView.findViewById(R.id.iv_coupon)
        btnAddQuota = itemView.findViewById(R.id.btn_add_quota)

        tvDate.text = "${item.voucherStartTime?.let { ProgramUpdateMapper.setDate(it) }} - ${item.voucherFinishTime?.let { ProgramUpdateMapper.setDate(it) }}"
        tvCouponTitle.text = item.voucherName
        when(item.minimumTierLevel){
            COUPON_VIP ->{
                tvMembership.text = "VIP"
                tvMembership.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.Unify_YN500)))
                tvMembership.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.Unify_YN100))
            }
            COUPON_MEMBER ->{
                tvMembership.text = "Premium"
                tvMembership.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.Unify_NN600)))
                tvMembership.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.Unify_NN100))
            }
        }
        tvQuota.text = "${item.voucherQuota}/${item.remainingQuota}"
        itemView.setOnClickListener {

        }

        if(item.remainingQuota == item.voucherQuota){
            btnAddQuota.show()
            btnAddQuota.setOnClickListener {
                item.voucherId?.let { it1 -> item.voucherTypeFormatted?.let { it2 ->
                    item.voucherQuota?.let { it3 ->
                        tmCouponActions.option(ADD_QUOTA, it1,
                            it2, it3
                        )
                    }
                } }
            }
        }
        else{
            btnAddQuota.hide()
        }

        when(item.voucherStatus){
            COUPON_DELETED ->{
            }
            COUPON_PROCESSING ->{
            }
            COUPON_NOT_STARTED ->{
                optionMenu.setOnClickListener {
                    item.voucherId?.let { it1 ->
                        val actions = Actions()
                        val tripleDots = arrayListOf<TripleDotsItem?>()
                        tripleDots.add(TripleDotsItem("Ubah Kupon", EDIT))
                        tripleDots.add(TripleDotsItem("Hapus Kupon", DELETE))
                        actions.tripleDots = tripleDots
                        item.voucherTypeFormatted?.let { it2 ->
                            item.voucherQuota?.let { it3 ->
                                TokomemberOptionsMenuBottomsheet.show(Gson().toJson(actions), fragmentManager, tmCouponActions, it1,
                                    it2,
                                    it3
                                )
                            }
                        }
                    }
                }
                tvCouponState.text = "Belum Aktif"
                tvCouponState.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.Unify_NN400)))
                viewStatus.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.Unify_NN400))
            }
            COUPON_ON_GOING ->{
                optionMenu.setOnClickListener {
                    item.voucherId?.let { it1 ->
                        val actions = Actions()
                        val tripleDots = arrayListOf<TripleDotsItem?>()
                        tripleDots.add(TripleDotsItem("Tambah Kuota", ADD_QUOTA))
                        /*
                            Not in phase 1
                            tripleDots.add(TripleDotsItem("Bakikan", SHARE))
                        */
                        tripleDots.add(TripleDotsItem("Hentikan Kupon", STOP))
                        actions.tripleDots = tripleDots
                        item.voucherTypeFormatted?.let { it2 ->
                            item.voucherQuota?.let { it3 ->
                                TokomemberOptionsMenuBottomsheet.show(Gson().toJson(actions), fragmentManager, tmCouponActions, it1,
                                    it2,
                                    it3
                                )
                            }
                        }
                    }
                }
                ivCoupon.loadImage(R.drawable.ic_tm_member_golden)
                tvCouponState.text = "Kupon Aktif"
                tvCouponState.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.Unify_GN500)))
                viewStatus.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.Unify_GN500))
            }
            COUPON_ENDED or COUPON_STOPPED ->{
                tvCouponState.text = "Berakhir"
                tvCouponState.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.Unify_NN400)))
                viewStatus.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.Unify_NN400))
            }
        }
        if(item.voucherStatus == COUPON_ON_GOING && item.remainingQuota == 0){
            optionMenu.setOnClickListener {
                item.voucherId?.let { it1 ->
                    val actions = Actions()
                    val tripleDots = arrayListOf<TripleDotsItem?>()
                    tripleDots.add(TripleDotsItem("Hentikan Kupon", STOP))
                    actions.tripleDots = tripleDots
                    item.voucherTypeFormatted?.let { it2 ->
                        item.voucherQuota?.let { it3 ->
                            TokomemberOptionsMenuBottomsheet.show(Gson().toJson(actions), fragmentManager, tmCouponActions, it1,
                                it2,
                                it3
                            )
                        }
                    }
                }
            }
        }
    }
}