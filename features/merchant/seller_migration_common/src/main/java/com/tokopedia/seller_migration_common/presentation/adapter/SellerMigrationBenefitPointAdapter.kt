package com.tokopedia.seller_migration_common.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller_migration_common.R
import kotlinx.android.synthetic.main.item_seller_migration_benefit_point.view.*

class SellerMigrationBenefitPointAdapter(var benefitPoints: List<CharSequence>): RecyclerView.Adapter<SellerMigrationBenefitPointAdapter.SellerMigrationBenefitPointViewHolder>() {

    inner class SellerMigrationBenefitPointViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerMigrationBenefitPointViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_seller_migration_benefit_point, parent, false)
        return SellerMigrationBenefitPointViewHolder(view)
    }

    override fun getItemCount(): Int = benefitPoints.size

    override fun onBindViewHolder(holder: SellerMigrationBenefitPointViewHolder, position: Int) {
        holder.itemView.tv_seller_migration_benefit_point?.text = benefitPoints.getOrNull(position) ?: ""
    }
}