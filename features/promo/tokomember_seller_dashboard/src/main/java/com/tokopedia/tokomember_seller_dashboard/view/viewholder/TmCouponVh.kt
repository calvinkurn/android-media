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
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmCouponActions
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmCouponDetailCallback
import com.tokopedia.tokomember_seller_dashboard.model.Actions
import com.tokopedia.tokomember_seller_dashboard.model.TripleDotsItem
import com.tokopedia.tokomember_seller_dashboard.model.VouchersItem
import com.tokopedia.tokomember_seller_dashboard.tracker.TmTracker
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
import com.tokopedia.tokomember_seller_dashboard.util.DUPLICATE
import com.tokopedia.tokomember_seller_dashboard.util.EDIT
import com.tokopedia.tokomember_seller_dashboard.util.SHARE
import com.tokopedia.tokomember_seller_dashboard.util.STOP
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.setDatePreview
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberOptionsMenuBottomsheet
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

const val VIP = "VIP"
const val PREMIUM = "Premium"

class TmCouponVh(itemView: View, private val fragmentManager: FragmentManager) :
    RecyclerView.ViewHolder(itemView) {

    private lateinit var tvCouponState: Typography
    lateinit var tvDate: Typography
    lateinit var tvCouponTitle: Typography
    lateinit var tvMembership: Typography
    lateinit var tvQuota: Typography
    lateinit var viewStatus: View
    lateinit var optionMenu: IconUnify
    lateinit var ivCoupon: ImageUnify
    lateinit var btnAddQuota: UnifyButton

    @SuppressLint("ResourcePackage", "SetTextI18n")
    fun bind(item: VouchersItem, tmCouponActions: TmCouponActions,callback: TmCouponDetailCallback?,tmTracker:TmTracker?) {

        viewStatus = itemView.findViewById(R.id.view_status)
        tvCouponState = itemView.findViewById(R.id.tv_coupon_state)
        tvDate = itemView.findViewById(R.id.tv_date)
        tvCouponTitle = itemView.findViewById(R.id.tv_coupon_title)
        tvMembership = itemView.findViewById(R.id.tv_membership)
        tvQuota = itemView.findViewById(R.id.tv_quota)
        optionMenu = itemView.findViewById(R.id.icon_options)
        ivCoupon = itemView.findViewById(R.id.iv_coupon)
        btnAddQuota = itemView.findViewById(R.id.btn_add_quota)

        tvDate.text = "${item.voucherStartTime?.let { setDatePreview(it.replace("T", " ").replace("Z", "")) }} - ${
            item.voucherFinishTime?.let {
                setDatePreview(it.replace("T", " ").replace("Z", ""))
            }
        }"
        tvCouponTitle.text = item.voucherName
        when (item.minimumTierLevel) {
            COUPON_VIP -> {
                tvMembership.text = VIP
                tvMembership.setTextColor(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_YN500
                        )
                    )
                )
                tvMembership.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_YN100
                    )
                )
            }
            COUPON_MEMBER -> {
                tvMembership.text = PREMIUM
                tvMembership.setTextColor(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_NN600
                        )
                    )
                )
                tvMembership.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN100
                    )
                )
            }
        }
        tvQuota.text = "${item.usedGlobalQuota}/${item.voucherQuota}"
        itemView.setOnClickListener {
            item.voucherStatus
        }

        when (item.voucherStatus) {
            COUPON_DELETED -> {
                optionMenu.hide()
                btnAddQuota.hide()
                tvCouponState.text = "Dihapus"
                tvCouponState.setTextColor(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_NN400
                        )
                    )
                )
                viewStatus.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN400
                    )
                )
            }
            COUPON_PROCESSING -> {
                optionMenu.hide()
                btnAddQuota.hide()
                tvCouponState.text = "Pengolahan"
                tvCouponState.setTextColor(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_NN400
                        )
                    )
                )
                viewStatus.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN400
                    )
                )
            }
            COUPON_NOT_STARTED -> {
                ivCoupon.loadImage(R.drawable.tm_ic_member_grey)
                optionMenu.show()
                optionMenu.setOnClickListener {
                    item.voucherId?.let { it1 ->
                        val actions = Actions()
                        val tripleDots = arrayListOf<TripleDotsItem?>()
                        tripleDots.add(TripleDotsItem("Ubah Kupon", EDIT))
                        tripleDots.add(TripleDotsItem("Hapus Kupon", DELETE))
                        actions.tripleDots = tripleDots
                        item.voucherTypeFormatted?.let { it2 ->
                            item.voucherQuota?.let { it3 ->
                                TokomemberOptionsMenuBottomsheet.show(
                                    Gson().toJson(actions), fragmentManager, tmCouponActions, it1,
                                    it2,
                                    it3
                                )
                            }
                        }
                    }
                }
                tvCouponState.text = "Belum Aktif"
                tvCouponState.setTextColor(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_NN400
                        )
                    )
                )
                viewStatus.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN400
                    )
                )
            }
            COUPON_ON_GOING -> {
                optionMenu.setOnClickListener {
                    item.voucherId.let { it1 ->
                        tmTracker?.clickCouponListThreeDot(item.shopId)
                        val actions = Actions()
                        val tripleDots = arrayListOf<TripleDotsItem?>()
                        tripleDots.add(TripleDotsItem("Tambah Kuota", ADD_QUOTA))
                        tripleDots.add(TripleDotsItem("Bagikan", SHARE))
                        tripleDots.add(TripleDotsItem("Hentikan Kupon", STOP))
                        actions.tripleDots = tripleDots
                        item.voucherTypeFormatted?.let { it2 ->
                            item.voucherQuota?.let { it3 ->
                                item.voucherDiscountAmtMax?.let { it4 ->
                                    TokomemberOptionsMenuBottomsheet.show(
                                        Gson().toJson(actions),
                                        fragmentManager,
                                        tmCouponActions,
                                        it1,
                                        it2,
                                        it3,
                                        it4
                                    )
                                }
                            }
                        }
                    }
                }
                optionMenu.show()
                ivCoupon.loadImage(R.drawable.tm_ic_member_golden)
                tvCouponState.text = "Kupon Aktif"
                tvCouponState.setTextColor(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_GN500
                        )
                    )
                )
                viewStatus.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                )
            }
            COUPON_ENDED -> {
                ivCoupon.loadImage(R.drawable.tm_ic_member_grey)
                optionMenu.show()
                btnAddQuota.hide()
                tvCouponState.text = "Berakhir"
                tvCouponState.setTextColor(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_NN400
                        )
                    )
                )
                viewStatus.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN400
                    )
                )
                optionMenu.setOnClickListener {
                    val actions = Actions()
                    val tripleDots = arrayListOf<TripleDotsItem?>()
                    tripleDots.add(TripleDotsItem("Duplikat Kupon", DUPLICATE))
                    actions.tripleDots = tripleDots
                    TokomemberOptionsMenuBottomsheet.show(
                        Gson().toJson(actions), fragmentManager, tmCouponActions, item.voucherId,
                        item.voucherTypeFormatted,
                        0
                    )
                }
            }
            COUPON_STOPPED -> {
                ivCoupon.loadImage(R.drawable.tm_ic_member_grey)
                optionMenu.show()
                btnAddQuota.hide()
                tvCouponState.text = "Berakhir"
                tvCouponState.setTextColor(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_NN400
                        )
                    )
                )
                viewStatus.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN400
                    )
                )
                optionMenu.setOnClickListener {
                    val actions = Actions()
                    val tripleDots = arrayListOf<TripleDotsItem?>()
                    tripleDots.add(TripleDotsItem("Duplikat Kupon", DUPLICATE))
                    actions.tripleDots = tripleDots
                    TokomemberOptionsMenuBottomsheet.show(
                        Gson().toJson(actions), fragmentManager, tmCouponActions, item.voucherId,
                        item.voucherTypeFormatted,
                        0
                    )
                }

            }
        }

        if (item.remainingQuota == 0) {
            tvCouponState.text = "Kuota Habis"
            viewStatus.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_YN500
                )
            )
            tvCouponState.setTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_YN500
                    )
                )
            )
            btnAddQuota.show()
            btnAddQuota.setOnClickListener {
                tmTracker?.clickBsAddQuotaButton(item.shopId)
                item.voucherId?.let { it1 ->
                    item.voucherTypeFormatted?.let { it2 ->
                        item.voucherQuota?.let { it3 ->
                            item.voucherDiscountAmtMax?.let { it4 ->
                                tmCouponActions.option(
                                    ADD_QUOTA, it1,
                                    it2.lowercase(), it3, it4
                                )
                            }
                        }
                    }
                }
            }
        } else {
            btnAddQuota.hide()
        }

        if (item.voucherStatus == COUPON_ON_GOING && item.remainingQuota == 0) {
            optionMenu.setOnClickListener {
                item.voucherId?.let { it1 ->
                    val actions = Actions()
                    val tripleDots = arrayListOf<TripleDotsItem?>()
                    tripleDots.add(TripleDotsItem("Hentikan Kupon", STOP))
                    actions.tripleDots = tripleDots
                    item.voucherTypeFormatted?.let { it2 ->
                        item.voucherQuota?.let { it3 ->
                            TokomemberOptionsMenuBottomsheet.show(
                                Gson().toJson(actions), fragmentManager, tmCouponActions, it1,
                                it2,
                                it3,
                            )
                        }
                    }
                }
            }
        }

        itemView.setOnClickListener {
            tmTracker?.clickSpecificCoupon(item.shopId)
            callback?.openCouponDetailFragment(item.voucherId.toIntOrZero())
        }
    }

    companion object{
        val LAYOUT = R.layout.tm_coupon_list_item
    }
}
