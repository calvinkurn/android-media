package com.tokopedia.hotel.roomdetail.view.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.presentation.widget.FacilityTextView
import com.tokopedia.hotel.common.presentation.widget.InfoTextView
import com.tokopedia.hotel.roomdetail.di.HotelRoomDetailComponent
import com.tokopedia.hotel.roomdetail.view.activity.HotelRoomDetailActivity
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import kotlinx.android.synthetic.main.fragment_hotel_room_detail.*
import kotlinx.android.synthetic.main.widget_info_text_view.view.*

/**
 * @author by resakemal on 23/04/19
 */

class HotelRoomDetailFragment: BaseDaggerFragment(){

    lateinit var hotelRoom: HotelRoom

    lateinit var saveInstanceCacheManager: SaveInstanceCacheManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        saveInstanceCacheManager = SaveInstanceCacheManager(activity!!, savedInstanceState)

        hotelRoom = saveInstanceCacheManager.get(EXTRA_ROOM_DATA, HotelRoom::class.java, HotelRoom())!!
        initDummyRoomDetail()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_room_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    fun initDummyRoomDetail() {
        val dummyRoomDetail = GraphqlHelper.loadRawString(resources, R.raw.dummy_hotel_room_detail)
        hotelRoom = Gson().fromJson(dummyRoomDetail, HotelRoom::class.java)
    }

    fun initView() {
        setupCollapsingToolbar()
        setupRoomImages()
        setupRoomHeader()
        setupRoomPayAtHotel()
        setupRoomCancellation()
        setupRoomTax()
        setupRoomDeposit()
        setupRoomFacilities()
        setupRoomDescription()
        setupRoomBreakfast()
        setupRoomExtraBed()
        setupRoomPrice()
    }

    fun setupCollapsingToolbar() {
        (activity as HotelRoomDetailActivity).setSupportActionBar(detail_toolbar)
        (activity as HotelRoomDetailActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        collapsing_toolbar.title = ""
        app_bar_layout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsing_toolbar.title = hotelRoom.roomInfo.name
                    isShow = true
                } else if (isShow) {
                    collapsing_toolbar.title = " "
                    isShow = false
                }
            }
        })
    }

    fun setupRoomImages() {
        if (!hotelRoom.roomInfo.roomImages.isEmpty()) {
            val roomDetailImages = hotelRoom.roomInfo.roomImages.map { it.url300 }
            room_detail_images.setImages(roomDetailImages)
        }
        room_detail_images.buildView()
    }

    fun setupRoomHeader() {
        tv_room_detail_title.setText(hotelRoom.roomInfo.name)
        tv_room_detail_occupancy.setText(getString(R.string.hotel_room_detail_header_occupancy,
                hotelRoom.occupancyInfo.occupancyText))
        tv_room_detail_size.setText(hotelRoom.bedInfo)

        val breakfastTextView = FacilityTextView(context!!)
        breakfastTextView.setIconAndText("",
                if (hotelRoom.breakfastInfo.isBreakfastIncluded) getString(R.string.hotel_room_list_free_breakfast)
                else getString(R.string.hotel_room_list_breakfast_not_included))
        room_detail_header_facilities.addView(breakfastTextView)

        val refundableTextView = FacilityTextView(context!!)
        refundableTextView.setIconAndText("",
                if (hotelRoom.refundInfo.isRefundable) getString(R.string.hotel_room_list_refundable_with_condition)
                else getString(R.string.hotel_room_list_not_refundable) )
        room_detail_header_facilities.addView(refundableTextView)

        if (hotelRoom.numberRoomLeft <= MINIMUM_ROOM_COUNT) {
            tv_room_detail_count.setText(getString(R.string.hotel_room_room_left_text,
                    Integer.toString(hotelRoom.numberRoomLeft)))
            tv_room_detail_count.visibility = View.VISIBLE
        }
    }

    fun setupRoomPayAtHotel() {
        val spannableString = SpannableString("  " + getString(R.string.hotel_room_detail_pay_at_hotel_desc))
        val icon = ContextCompat.getDrawable(context!!, R.drawable.ic_hotel_calendar)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), 2, 28, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(ImageSpan(icon, ImageSpan.ALIGN_BASELINE), 0, 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv_room_detail_pay_at_hotel_desc.setText(spannableString)
    }

    fun setupRoomCancellation() {
        if (!hotelRoom.cancelPolicy.isEmpty()) {
            val spannableStringBuilder = SpannableStringBuilder()
            for (policy in hotelRoom.cancelPolicy) {
                val start = spannableStringBuilder.length
                spannableStringBuilder.append(policy.subheader)
                val end = spannableStringBuilder.length
                spannableStringBuilder.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableStringBuilder.append("\n" + policy.content + "\n\n")
            }
            spannableStringBuilder.delete(spannableStringBuilder.length - 2, spannableStringBuilder.length)

            room_detail_cancellation.infoTitle = getString(R.string.hotel_room_detail_cancellation)
            room_detail_cancellation.infoDescription = spannableStringBuilder
            room_detail_cancellation.truncateDescription = false
            room_detail_cancellation.buildView()
        }
    }

    fun setupRoomTax() {
        if (!hotelRoom.taxes.isEmpty()) {
            room_detail_tax.infoTitle = getString(R.string.hotel_room_detail_tax)
            room_detail_tax.infoDescription = hotelRoom.taxes
            room_detail_tax.buildView()
        }
    }

    fun setupRoomDeposit() {
        if (!hotelRoom.depositInfo.depositText.isEmpty()) {
            room_detail_deposit.infoTitle = getString(R.string.hotel_room_detail_deposit)
            room_detail_deposit.infoDescription = hotelRoom.depositInfo.depositText
            room_detail_deposit.buildView()
        }
    }

    fun setupRoomFacilities() {
        if (!hotelRoom.roomInfo.facility.isEmpty()) {
            val facilityList = hotelRoom.roomInfo.facility
            val stringBuilder = StringBuffer()
            var previewFacilitiesString = ""
            var fullFacilitiesString = ""

            for (i in 0..facilityList.size - 1) {
                stringBuilder.append(getString(R.string.hotel_room_detail_facility_item, facilityList[i].name))
                stringBuilder.append("\n")
                if (i == ROOM_FACILITY_DEFAULT_COUNT - 1) {
                    previewFacilitiesString = stringBuilder.toString()
                }
            }
            if (previewFacilitiesString.isEmpty()) previewFacilitiesString = stringBuilder.toString()
            fullFacilitiesString = stringBuilder.toString()
            previewFacilitiesString = previewFacilitiesString.dropLast(1)
            fullFacilitiesString = fullFacilitiesString.dropLast(1)

            room_detail_facilities.infoTitle = getString(R.string.hotel_room_detail_facilities)
            room_detail_facilities.infoDescription = previewFacilitiesString
            room_detail_facilities.info_more.visibility = View.VISIBLE
            room_detail_facilities.infoViewListener = object : InfoTextView.InfoViewListener {
                override fun onMoreClicked() {
                    room_detail_facilities.info_desc.setText(fullFacilitiesString)
                    room_detail_facilities.resetMaxLineCount()
                    room_detail_facilities.invalidate()
                }
            }
            room_detail_facilities.truncateDescription = false
            room_detail_facilities.descriptionLineCount = ROOM_FACILITY_DEFAULT_COUNT
            room_detail_facilities.buildView()
        }
    }

    fun setupRoomDescription() {
        if (!hotelRoom.roomInfo.description.isEmpty()) {
            room_detail_description.infoTitle = getString(R.string.hotel_room_detail_description)
            room_detail_description.infoDescription = hotelRoom.roomInfo.description
            room_detail_description.buildView()
        }
    }

    fun setupRoomBreakfast() {
        if (!hotelRoom.breakfastInfo.mealPlan.isEmpty()) {
            room_detail_breakfast.infoTitle = getString(R.string.hotel_room_detail_breakfast)
            room_detail_breakfast.infoDescription = hotelRoom.breakfastInfo.mealPlan
            room_detail_breakfast.buildView()
        }
    }

    fun setupRoomExtraBed() {
        if (!hotelRoom.extraBedInfo.content.isEmpty()) {
            room_detail_extra_bed.infoTitle = getString(R.string.hotel_room_detail_extra_bed)
            room_detail_extra_bed.infoDescription = hotelRoom.extraBedInfo.content
            room_detail_extra_bed.buildView()
        }
    }

    fun setupRoomPrice() {
        tv_room_detail_price.setText(hotelRoom.roomPrice[0].roomPrice)
        room_detail_button.setText(getString(R.string.hotel_room_list_choose_room_button, ""))
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HotelRoomDetailComponent::class.java).inject(this)
    }

    companion object {
        const val EXTRA_ROOM_DATA = "extra_room_data"

        val MINIMUM_ROOM_COUNT = 3
        val ROOM_FACILITY_DEFAULT_COUNT = 6

        fun getInstance(): HotelRoomDetailFragment = HotelRoomDetailFragment()
    }
}