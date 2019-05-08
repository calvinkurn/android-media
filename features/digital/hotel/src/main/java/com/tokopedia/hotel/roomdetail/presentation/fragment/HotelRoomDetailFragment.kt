package com.tokopedia.hotel.roomdetail.presentation.fragment

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
import com.tokopedia.hotel.roomdetail.presentation.activity.HotelRoomDetailActivity
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import kotlinx.android.synthetic.main.fragment_hotel_room_detail.*
import kotlinx.android.synthetic.main.widget_info_text_view.view.*

/**
 * @author by resakemal on 23/04/19
 */

class HotelRoomDetailFragment : BaseDaggerFragment() {

    lateinit var hotelRoom: HotelRoom

    lateinit var saveInstanceCacheManager: SaveInstanceCacheManager

    override fun onCreate(savedInstanceState: Bundle?) {
        saveInstanceCacheManager = SaveInstanceCacheManager(activity!!, savedInstanceState)
        val manager = if (savedInstanceState == null) SaveInstanceCacheManager(activity!!,
                arguments!!.getString(HotelRoomDetailActivity.EXTRA_SAVED_INSTANCE_ID)) else saveInstanceCacheManager

//        hotelRoom = manager.get(EXTRA_ROOM_DATA, HotelRoom::class.java, HotelRoom())!!
        hotelRoom =  Gson().fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_hotel_room_detail), HotelRoom::class.java)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_room_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveInstanceCacheManager.onSave(outState)
        saveInstanceCacheManager.put(HotelRoomDetailFragment.EXTRA_ROOM_DATA, hotelRoom)
    }

    private fun initView() {
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

    private fun setupCollapsingToolbar() {
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

    private fun setupRoomImages() {
        if (hotelRoom.roomInfo.roomImages.isNotEmpty()) {
            val roomImageUrls300 = hotelRoom.roomInfo.roomImages.map { it.url300 }
            room_detail_images.setImages(roomImageUrls300)
        }
        room_detail_images.buildView()
    }

    private fun setupRoomHeader() {
        tv_room_detail_title.text = hotelRoom.roomInfo.name
        tv_room_detail_occupancy.text = getString(R.string.hotel_room_detail_header_occupancy,
                hotelRoom.occupancyInfo.occupancyText)
        tv_room_detail_size.text = hotelRoom.bedInfo

        val breakfastTextView = FacilityTextView(context!!)
        breakfastTextView.setIconAndText("",
                if (hotelRoom.breakfastInfo.isBreakfastIncluded) getString(R.string.hotel_room_list_free_breakfast)
                else getString(R.string.hotel_room_list_breakfast_not_included))
        room_detail_header_facilities.addView(breakfastTextView)

        val refundableTextView = FacilityTextView(context!!)
        refundableTextView.setIconAndText("",
                if (hotelRoom.refundInfo.isRefundable) getString(R.string.hotel_room_list_refundable_with_condition)
                else getString(R.string.hotel_room_list_not_refundable))
        room_detail_header_facilities.addView(refundableTextView)

        if (hotelRoom.numberRoomLeft <= MINIMUM_ROOM_COUNT) {
            tv_room_detail_count.visibility = View.VISIBLE
            tv_room_detail_count.text = getString(R.string.hotel_room_room_left_text,
                    Integer.toString(hotelRoom.numberRoomLeft))
        }
    }

    fun setupRoomPayAtHotel() {
        if (!hotelRoom.additionalPropertyInfo.isDirectPayment) {
            val spannableString = SpannableString("   " + hotelRoom.creditCardInfo.creditCardInfo
                    + getString(R.string.hotel_room_detail_pay_at_hotel_desc))
            val iconId = if (hotelRoom.additionalPropertyInfo.isCvCRequired)
                R.drawable.ic_pay_at_hotel_cc else R.drawable.ic_pay_at_hotel_no_cc
            val icon = ContextCompat.getDrawable(context!!, iconId)
            icon?.setBounds(0, -6, 50, 50)
            spannableString.setSpan(StyleSpan(Typeface.BOLD), 3, 3 + hotelRoom.creditCardInfo.creditCardInfo.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(ImageSpan(icon!!, ImageSpan.ALIGN_BASELINE), 0, 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            room_detail_pay_at_hotel.setTitleAndDescription(getString(R.string.hotel_room_detail_pay_at_hotel), spannableString)
            room_detail_pay_at_hotel.truncateDescription = false
            room_detail_pay_at_hotel.buildView()
        }
    }

    fun setupRoomCancellation() {
        if (hotelRoom.cancelPolicy.isNotEmpty()) {
            val spannableStringBuilder = SpannableStringBuilder()
            for (policy in hotelRoom.cancelPolicy) {
                val start = spannableStringBuilder.length
                spannableStringBuilder.append(policy.subheader)
                val end = spannableStringBuilder.length
                spannableStringBuilder.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableStringBuilder.append("\n" + policy.content + "\n\n")
            }
            spannableStringBuilder.delete(spannableStringBuilder.length - 2, spannableStringBuilder.length)

            room_detail_cancellation.setTitleAndDescription(getString(R.string.hotel_room_detail_cancellation), spannableStringBuilder)
            room_detail_cancellation.truncateDescription = false
            room_detail_cancellation.buildView()
        }
    }

    fun setupRoomTax() {
        if (hotelRoom.taxes.isNotEmpty()) {
            room_detail_tax.setTitleAndDescription(getString(R.string.hotel_room_detail_tax), hotelRoom.taxes)
            room_detail_tax.buildView()
        }
    }

    fun setupRoomDeposit() {
        if (hotelRoom.depositInfo.depositText.isNotEmpty()) {
            room_detail_deposit.setTitleAndDescription(getString(R.string.hotel_room_detail_deposit), hotelRoom.depositInfo.depositText)
            room_detail_deposit.buildView()
        }
    }

    fun setupRoomFacilities() {
        if (hotelRoom.roomInfo.facility.isNotEmpty()) {
            val facilityList = hotelRoom.roomInfo.facility
            val stringBuilder = StringBuffer()
            var previewFacilitiesString = ""
            var fullFacilitiesString: String

            for (i in 0 until facilityList.size) {
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

            room_detail_facilities.setTitleAndDescription(getString(R.string.hotel_room_detail_facilities), previewFacilitiesString)
            room_detail_facilities.info_more.visibility = View.VISIBLE
            room_detail_facilities.infoViewListener = object : InfoTextView.InfoViewListener {
                override fun onMoreClicked() {
                    room_detail_facilities.info_desc.text = fullFacilitiesString
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
        if (hotelRoom.roomInfo.description.isNotEmpty()) {
            room_detail_description.setTitleAndDescription(getString(R.string.hotel_room_detail_description), hotelRoom.roomInfo.description)
            room_detail_description.buildView()
        }
    }

    fun setupRoomBreakfast() {
        if (hotelRoom.breakfastInfo.mealPlan.isNotEmpty()) {
            room_detail_breakfast.setTitleAndDescription(getString(R.string.hotel_room_detail_breakfast), hotelRoom.breakfastInfo.mealPlan)
            room_detail_breakfast.buildView()
        }
    }

    fun setupRoomExtraBed() {
        if (hotelRoom.extraBedInfo.content.isNotEmpty()) {
            room_detail_extra_bed.setTitleAndDescription(getString(R.string.hotel_room_detail_extra_bed), hotelRoom.extraBedInfo.content)
            room_detail_extra_bed.buildView()
        }
    }

    fun setupRoomPrice() {
        if (hotelRoom.roomPrice.isNotEmpty())
        tv_room_detail_price.text = hotelRoom.roomPrice[0].roomPrice
        room_detail_button.text = getString(R.string.hotel_room_list_choose_room_button)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HotelRoomDetailComponent::class.java).inject(this)
    }

    companion object {
        const val EXTRA_ROOM_DATA = "extra_room_data"

        const val MINIMUM_ROOM_COUNT = 3
        const val ROOM_FACILITY_DEFAULT_COUNT = 6

        fun getInstance(savedInstanceId: String): HotelRoomDetailFragment =
                HotelRoomDetailFragment().also {
                    it.arguments = Bundle().apply {
                        putString(HotelRoomDetailActivity.EXTRA_SAVED_INSTANCE_ID, savedInstanceId)
                    }
                }
    }
}