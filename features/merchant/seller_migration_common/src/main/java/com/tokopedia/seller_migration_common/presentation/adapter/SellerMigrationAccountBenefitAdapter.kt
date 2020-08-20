package com.tokopedia.seller_migration_common.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.data.source.AccountMigrationBenefitProvider
import kotlinx.android.synthetic.main.item_seller_migration_account_comm.view.*

class SellerMigrationAccountBenefitAdapter(context: Context): RecyclerView.Adapter<SellerMigrationAccountBenefitAdapter.SellerMigrationAccountBenefitViewHolder>() {

    inner class SellerMigrationAccountBenefitViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val benefitList = AccountMigrationBenefitProvider.getBenefitList(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerMigrationAccountBenefitViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_seller_migration_account_comm, parent, false)
        return SellerMigrationAccountBenefitViewHolder(view)
    }

    override fun getItemCount(): Int = benefitList.size

    override fun onBindViewHolder(holder: SellerMigrationAccountBenefitViewHolder, position: Int) {
        val benefit = benefitList.getOrNull(position)
        with(holder.itemView) {
            benefit?.drawableRes?.let { res ->
                iv_item_seller_migration_account?.loadImageDrawable(res)
            }
            tv_item_seller_migration_account_title?.text = benefit?.title.orEmpty()
            tv_item_seller_migration_account_desc?.text = benefit?.description.orEmpty()
        }
    }
}