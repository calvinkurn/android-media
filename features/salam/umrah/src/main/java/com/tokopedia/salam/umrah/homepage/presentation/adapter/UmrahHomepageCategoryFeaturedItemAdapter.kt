package com.tokopedia.salam.umrah.homepage.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.util.CurrencyFormatter.getRupiahFormat
import com.tokopedia.salam.umrah.common.util.UmrahDateUtil
import com.tokopedia.salam.umrah.common.util.UmrahHotelRating
import com.tokopedia.salam.umrah.common.util.UmrahPriceUtil.getSlashedPrice
import com.tokopedia.salam.umrah.homepage.data.Products
import com.tokopedia.salam.umrah.homepage.presentation.listener.onItemBindListener
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpActivity
import com.tokopedia.unifycomponents.Label
import kotlinx.android.synthetic.main.widget_umrah_homepage_deals.view.*

/**
 * @author by firman on 22/10/19
 */

class UmrahHomepageCategoryFeaturedItemAdapter(val onItemBindListener: onItemBindListener): RecyclerView.Adapter<UmrahHomepageCategoryFeaturedItemAdapter.UmrahHomepageCategoryFeaturedItemViewHolder>(){
    private var listCategories = emptyList<Products>()
    private var headerTitle = ""
    private var positionDC = 0

    inner class UmrahHomepageCategoryFeaturedItemViewHolder (view: View): RecyclerView.ViewHolder(view){

        val labelUmrahDuration : Label = view.findViewById(R.id.label_umrah_duration)

        fun bind(products: Products){
            with(itemView) {

                container_umrah_homepage_deals_shimmering.visibility = View.GONE
                iv_umrah_image.loadImage(products.banner[0])
                labelUmrahDuration.text = resources.getString(R.string.umrah_home_page_days_duration,products.durationDays.toString())
                iv_umrah_provider_logo.loadImage(products.travelAgent.logoUrl)
                tg_umrah_title.text = products.title
                tg_umrah_mulai_dari.text = getSlashedPrice(resources,products.slashPrice)
                tg_umrah_price.text = getRupiahFormat(products.originalPrice)
                tg_umrah_calendar.text = resources.getString(R.string.umrah_home_page_departure_back_date,
                        UmrahDateUtil.getTime(UmrahDateUtil.DATE_WITHOUT_YEAR_FORMAT, products.departureDate),
                        UmrahDateUtil.getTime(UmrahDateUtil.DATE_WITH_YEAR_FORMAT,products.returningDate))
                tg_umrah_hotel.text = resources.getString(R.string.umrah_home_page_hotel_stars,
                        UmrahHotelRating.getAllHotelRatings(products.hotels))
                tg_umrah_plane.text = products.airlines[0].name
                container_umrah_homepage_deals.visibility = View.VISIBLE
                container_umrah_homepage_deals.setOnClickListener {
                    onItemBindListener.onClickFeaturedCategory(headerTitle,positionDC,products,position)
                    context?.let {
                        context.startActivity(
                                UmrahPdpActivity.createIntent(it,
                                        products.slugName
                                ))
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = listCategories.size
    override fun onBindViewHolder(holder: UmrahHomepageCategoryFeaturedItemViewHolder, position: Int) {
        holder.bind(listCategories[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UmrahHomepageCategoryFeaturedItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.widget_umrah_homepage_deals, parent, false)
        return UmrahHomepageCategoryFeaturedItemViewHolder(itemView)
    }

    fun setList(list: List<Products>, header: String, position:Int){
        listCategories = list
        headerTitle = header
        positionDC = position
    }

}