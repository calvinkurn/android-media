package com.tokopedia.mlp.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.widget.RecyclerView
import android.view.*
import android.webkit.URLUtil
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.example.merchant_lending_widget.R
import com.tokopedia.abstraction.common.utils.HexValidator
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.mlp.callback.MLPWidgetAdapterCallBack
import com.tokopedia.mlp.contractModel.*
import com.tokopedia.mlp.fragment.MerchantLendingFragment
import com.tokopedia.mlp.router.MLPRouter
import kotlinx.android.synthetic.main.fragment_merchant_lending.view.*
import kotlinx.android.synthetic.main.mlp_bottomsheet.view.*
import kotlinx.android.synthetic.main.mlp_box_layout.view.*
import kotlinx.android.synthetic.main.mlp_row_info.view.*
import kotlinx.android.synthetic.main.switchon_popup.view.*


class MLPWidgetAdapter(private val boxList: List<WidgetsItem>, val context: Context, var isexpanded: Boolean, var merchantLendingFragment: MerchantLendingFragment) : RecyclerView.Adapter<MLPWidgetAdapter.ViewHolder>() {

    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val bodyOpenBottomSheetType = 1
    private val sideTextOpenBottomSheetType = 2
    private val toggleOpenBottomSheetType = 3
    private var originalState: Boolean? = false
    private val paddingTopTextBody = 8
    private val paddingBottomTextBody = 10
    lateinit var mlpWidgetAdapterCallBack: MLPWidgetAdapterCallBack

    interface ToggleSaldoPrioritasLisneter {
        fun onSuccessToggleSaldo(success: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MLPWidgetAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_merchant_lending, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(boxList[position], position)
    }

    override fun getItemCount(): Int {
        return boxList.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindData(widgetsItem: WidgetsItem, position: Int) {
            if (!boxList[position].firstLoad) {
                if (isexpanded) {
                    itemView.ll_container.show()
                } else {
                    itemView.ll_container.hide()
                }
                return
            } else {
                widgetsItem.header?.let {
                    itemView.text_title.setTextAndCheckShow(it.title)
                    if (it.logo?.length!! > 0) {
                        itemView.header_logo.show()
                        ImageHandler.loadImage(context, itemView.header_logo, it.logo, R.color.grey_100)
                    } else {
                        itemView.header_logo.hide()
                    }
                    itemView.text_badge.setTextAndCheckShow(it.tag?.name)

                    if (HexValidator.validate(it.tag?.color)) {
                        itemView.text_badge.background.setColorFilter(Color.parseColor(it.tag?.color), PorterDuff.Mode.SRC_ATOP)
                    } else {
                        itemView.text_badge.background.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                    }
                    val checkToggleStatus: Boolean? = it.sideToggle?.showToggle
                    val checkSideTextSize = it.sideText?.text?.isNotEmpty()
                    if (checkToggleStatus!! && checkSideTextSize!!) {
                        renderSideTextHeader(it, position)
                    } else if (checkToggleStatus && !checkSideTextSize!!) {
                        renderSideToggleHeader(it, position)
                    } else if (checkSideTextSize!! && !checkToggleStatus)
                        renderSideTextHeader(it, position)
                    else {
                        itemView.text_side.hide()
                        itemView.switch_enable.hide()
                    }
                }

                widgetsItem.body?.let {
                    renderBodyInfo(it.info)
                    renderBodyBox(it.boxes)
                }

                widgetsItem.body?.urlbody?.let {
                    redirectUrl(it, position)
                }

                boxList[position].firstLoad = false
            }
        }

        private fun renderSideTextHeader(headerContent: Header, position: Int) {

            itemView.text_side.setTextAndCheckShow(headerContent.sideText?.text)
            itemView.text_side.setOnClickListener {
                headerContent.sideText?.url?.let {
                    openWebViewOrOpenBottomSheet(it, position, sideTextOpenBottomSheetType)
                }
            }
        }

        private fun renderSideToggleHeader(headerContent: Header, position: Int) {
            itemView.switch_enable.show()
            originalState = headerContent.sideToggle?.toggleStatus
            itemView.switch_enable.isChecked = originalState!!
            itemView.switch_enable.setOnCheckedChangeListener { buttonView, isChecked ->

                if (isChecked) {
                    if (originalState == isChecked) {
                        return@setOnCheckedChangeListener
                    }
                    mlpWidgetAdapterCallBack = merchantLendingFragment
                    mlpWidgetAdapterCallBack.toggleSaldoPrioritas(true, object : ToggleSaldoPrioritasLisneter {
                        override fun onSuccessToggleSaldo(success: Boolean) {
                            if (success == null || success) {
                                originalState = true
                                showPopUp()
                            } else {
                                print("spUpdate failed")
                            }
                        }
                    })
                } else {
                    val bottomSheetLength: Int = boxList[position].bottomSheet?.size!!
                    val bottomSheetItem = boxList[position].bottomSheet
                    val placeHolder: String? = boxList[position].header?.sideToggle?.url
                    val bottomSheetId: Int = computeBottomSheetId(placeHolder, position, bottomSheetLength)
                    if (bottomSheetId < 0) {
                        itemView.switch_enable.isEnabled = false
                        return@setOnCheckedChangeListener

                    } else
                        renderBottomSheet(bottomSheetItem?.get(bottomSheetId), position, toggleOpenBottomSheetType)
                }
            }
        }

        private fun renderBodyInfo(it: List<InfoItem?>?) {

            if (it?.isEmpty()!!) {
                itemView.info_container.hide()
            } else {

                if (itemView.info_container.childCount > 0) {
                    return
                }
                var infolabel: String?
                var infovalue: String?
                val length: Int = it.size

                for (item in 0 until length) {
                    infolabel = it.get(item)?.label
                    infovalue = it.get(item)?.value

                    val viewRowLayout = inflater.inflate(R.layout.mlp_row_info, null)
                    viewRowLayout.label.setTextAndCheckShow(infolabel)
                    viewRowLayout.value.setTextAndCheckShow(infovalue)
                    itemView.info_container.addView(viewRowLayout)
                }
                itemView.info_container.show()
            }
        }

        private fun renderBodyBox(it: List<BoxesItem?>?) {
            if (it?.isEmpty()!!) {
                itemView.box_container.hide()
            } else {
                if (itemView.box_container.childCount > 0) {
                    return
                }
                var boxTitle: String?
                var boxContent: String?
                var boxColor: String?
                val length: Int = it.size

                for (item in 0 until length) {

                    if (it[item]?.title?.length!! > 0 || it[item]?.text?.length!! > 0) {
                        boxTitle = it.get(item)?.title
                        boxContent = it.get(item)?.text
                        boxColor = it.get(item)?.boxColor
                        itemView.box_container.show()

                        val viewBoxLayout = inflater.inflate(R.layout.mlp_box_layout, null)
                        viewBoxLayout.text_body_title.setTextAndCheckShow(boxTitle)
                        if ((!viewBoxLayout.text_body_title.isVisible)) {
                            viewBoxLayout.text_body_content.setPadding(0, paddingTopTextBody, 0, paddingBottomTextBody)
                        } else {
                            viewBoxLayout.text_body_content.setPadding(0, viewBoxLayout.text_body_content.paddingTop, 0, paddingBottomTextBody)
                        }
                        viewBoxLayout.text_body_content.setTextAndCheckShow(boxContent)
                        viewBoxLayout.viewbody_background.show()

                        if (boxColor != null && boxColor.isNotEmpty()) {
                            viewBoxLayout.viewbody_background.background.setColorFilter(Color.parseColor(boxColor), PorterDuff.Mode.SRC_ATOP)
                        } else {
                            viewBoxLayout.viewbody_background.background.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                        }
                        itemView.box_container.addView(viewBoxLayout)
                    }
                }
                itemView.box_container.show()
            }
        }


        private fun renderBottomSheet(bottomSheetItem: BottomSheetItem?, position: Int, type: Int) {

            val closeableBottomSheetDialog: CloseableBottomSheetDialog = CloseableBottomSheetDialog.createInstanceRounded(context)
            val viewMLPBottomSheet: View = View.inflate(context, R.layout.mlp_bottomsheet, null)
            viewMLPBottomSheet.tv_title_bottom.text = bottomSheetItem?.title
            viewMLPBottomSheet.tv_detail_bottom.text = bottomSheetItem?.text
            viewMLPBottomSheet.button_bottomsheet.text = bottomSheetItem?.buttonCta

            viewMLPBottomSheet.button_bottomsheet.setOnClickListener {
                if (type == bodyOpenBottomSheetType || type == sideTextOpenBottomSheetType) {
                    val url: String? = bottomSheetItem?.url
                    url?.let {
                        openWebViewOrOpenBottomSheet(url, position, 0)
                    }
                } else if (type == toggleOpenBottomSheetType) {
                    mlpWidgetAdapterCallBack = merchantLendingFragment
                    mlpWidgetAdapterCallBack.toggleSaldoPrioritas(false, object : ToggleSaldoPrioritasLisneter {
                        override fun onSuccessToggleSaldo(success: Boolean) {
                            if (success == null || success) {
                                originalState = false
                                closeableBottomSheetDialog.dismiss()
                            } else {
                                print("SP DISABLE FAILED")
                            }
                        }
                    })
                }
            }

            closeableBottomSheetDialog.setCustomContentView(viewMLPBottomSheet, bottomSheetItem?.title, false)
            closeableBottomSheetDialog.show()
            viewMLPBottomSheet.iv_cancel.setOnClickListener {
                if (type == toggleOpenBottomSheetType) {
                    itemView.switch_enable.toggle()
                }
                closeableBottomSheetDialog.dismiss()
            }
            closeableBottomSheetDialog.setCancelable(false)
            closeableBottomSheetDialog.setCanceledOnTouchOutside(false)
        }

        private fun redirectUrl(urlBody: String?, position: Int) {
            urlBody?.let { url ->
                if (itemView.box_container.isVisible || itemView.info_container.isVisible) {
                    itemView.box_container.setOnClickListener {
                        openWebViewOrOpenBottomSheet(url, position, bodyOpenBottomSheetType)
                    }
                } else if (itemView.info_container.isVisible) {
                    itemView.info_container.setOnClickListener {
                        openWebViewOrOpenBottomSheet(url, position, bodyOpenBottomSheetType)
                    }
                } else {
                    itemView.box_container.hide()
                    itemView.box_container.hide()
                }
            }
        }

        private fun openWebViewOrOpenBottomSheet(rawUrl: String, position: Int, type: Int) {

            val filteredUrl: String
            val checkValidFilteredUrl: Boolean
            val checkValidRawUrl = rawUrl.startsWith("tokopedia", true)

            if (checkValidRawUrl) {
                filteredUrl = rawUrl.substring(rawUrl.indexOf('=') + 1)
                checkValidFilteredUrl = URLUtil.isValidUrl(filteredUrl)

                if (checkValidFilteredUrl) {
                    val mlpRouter = context.applicationContext as MLPRouter
                    context.startActivity(mlpRouter.getSellerWebViewIntent(context, filteredUrl))

                } else {
                    print("Not valid url")
                }
            } else {
                val bottomSheetLength: Int = boxList[position].bottomSheet?.size!!
                val bottomSheetItem = boxList[position].bottomSheet

                when (type) {

                    bodyOpenBottomSheetType -> {
                        val placeHolder: String? = boxList[position].body?.urlbody
                        val bottomSheetId: Int = computeBottomSheetId(placeHolder, position, bottomSheetLength)

                        if (bottomSheetId >= 0) {
                            renderBottomSheet(bottomSheetItem?.get(bottomSheetId), position, bodyOpenBottomSheetType)
                        }
                    }

                    sideTextOpenBottomSheetType -> {
                        val placeHolder: String? = boxList[position].header?.sideText?.url
                        val bottomSheetId: Int = computeBottomSheetId(placeHolder, position, bottomSheetLength)

                        if (bottomSheetId >= 0) {
                            renderBottomSheet(bottomSheetItem?.get(bottomSheetId), position, sideTextOpenBottomSheetType)
                        }
                    }
                }
            }
        }

        private fun computeBottomSheetId(placeHolder: String?, position: Int, bottomSheetLength: Int): Int {
            var index: Int = -1
            for (bottomItem in 0 until bottomSheetLength) {
                index++
                if (placeHolder == boxList[position].bottomSheet?.get(bottomItem)?.id) {
                    return index
                }
            }
            return index
        }

        private fun showPopUp() {
            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable = true
            val viewPopUp = inflater.inflate(R.layout.switchon_popup, null)
            val popupWindow = PopupWindow(viewPopUp, width, height, focusable)
            popupWindow.showAtLocation(viewPopUp, Gravity.BOTTOM, 0, 0)

            viewPopUp.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    popupWindow.dismiss()
                    return true
                }
            })
            viewPopUp.tv_dismiss.setOnClickListener {
                popupWindow.dismiss()
            }
        }
    }
}







