package com.tokopedia.mlp.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.*
import com.example.merchant_lending_widget.R
import com.tokopedia.abstraction.common.utils.HexValidator
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.mlp.contractModel.*
import com.tokopedia.mlp.router.MLPRouter
import kotlinx.android.synthetic.main.fragment_merchant_lending.view.*
import kotlinx.android.synthetic.main.mlp_box_layout.view.*
import kotlinx.android.synthetic.main.mlp_row_info.view.*
import kotlinx.android.synthetic.main.switchon_popup.view.*

class MLPWidgetAdapter(private val boxList: List<WidgetsItem>, val context: Context, var isexpanded: Boolean) : RecyclerView.Adapter<MLPWidgetAdapter.ViewHolder>() {

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
                    itemView.ll_container.visibility = View.VISIBLE
                } else {
                    itemView.ll_container.visibility = View.GONE
                }
                return
            } else {
                widgetsItem.header?.let {
                    itemView.text_title.setTextAndCheckShow(it.title)
                    if (it.logo?.length!! > 0) {
                        itemView.header_logo.visibility = View.VISIBLE
                        ImageHandler.loadImage(context, itemView.header_logo, it.logo, R.color.grey_100)
                    }


                    itemView.text_badge.setTextAndCheckShow(it.tag?.name)
                    if (HexValidator.validate(it.tag?.color)) {
                        itemView.text_badge.background.setColorFilter(Color.parseColor(it.tag?.color), PorterDuff.Mode.SRC_ATOP)
                    }


                    val checkToggleStatus: Boolean? = it.sideToggle?.toggleStatus
                    val checkSideTextSize = it.sideText?.text?.isNotEmpty()

                    if (checkToggleStatus!! && checkSideTextSize!!) {
                        renderSideTextHeader(it, position)
                    } else if (checkToggleStatus && !checkSideTextSize!!) {
                        renderSideToggleHeader(it, position)
                    } else if (checkSideTextSize!! && !checkToggleStatus)
                        renderSideTextHeader(it, position)
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


        private fun renderSideToggleHeader(headerContent: Header, position: Int) {

            itemView.switch_enable.visibility = View.VISIBLE

            itemView.switch_enable.setOnCheckedChangeListener { _, isChecked ->
                run {
                    headerContent.sideToggle?.url?.let {

                        if (!isChecked) {
                            openWebViewOrOpenBottomSheet(it, position, 2)
                        } else {
                            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                            val myView = inflater.inflate(R.layout.switchon_popup, null)
                            val width = LinearLayout.LayoutParams.WRAP_CONTENT
                            val height = LinearLayout.LayoutParams.WRAP_CONTENT
                            val focusable = true
                            val popupWindow = PopupWindow(myView, width, height, focusable)

                            popupWindow.showAtLocation(myView, Gravity.BOTTOM, 0, 0)

                            myView.tv_dismiss.setOnClickListener {
                                popupWindow.dismiss()
                            }

                        }
                    }
                }
            }
        }

        private fun renderSideTextHeader(headerContent: Header, position: Int) {

            itemView.text_side.setTextAndCheckShow(headerContent.sideText?.text)

            itemView.text_side.setOnClickListener {
                headerContent.sideText?.url?.let {
                    openWebViewOrOpenBottomSheet(it, position, 1)
                }
            }
        }

        private fun renderBodyInfo(it: List<InfoItem?>?) {

            if (it?.isEmpty()!!) {
                itemView.info_container.visibility = View.GONE

            } else {
                if (itemView.info_container.childCount > 0) {
                    return
                }

                var infolabel: String?
                var infovalue: String?
                var length: Int = it.size


                for (item in 0 until length) {
                    infolabel = it.get(item)?.label
                    infovalue = it.get(item)?.value

                    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val myView = inflater.inflate(R.layout.mlp_row_info, null)

                    myView.label.setTextAndCheckShow(infolabel)
                    myView.value.setTextAndCheckShow(infovalue)
                    itemView.info_container.addView(myView)
                }
                itemView.info_container.visibility = View.VISIBLE
            }

        }


        private fun renderBodyBox(it: List<BoxesItem?>?) {


            if (it?.isEmpty()!!) {
                itemView.box_container.visibility = View.GONE

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
                        itemView.box_container.visibility = View.VISIBLE

                        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                        val myView = inflater.inflate(R.layout.mlp_box_layout, null)

                        myView.text_body_title.setTextAndCheckShow(boxTitle)

                        if ((!myView.text_body_title.isVisible)) {
                            myView.text_body_content.setPadding(0, 8, 0, 8)
                        }
                        myView.text_body_content.setTextAndCheckShow(boxContent)
                        myView.viewbody_background.visibility = View.VISIBLE

                        if (boxColor != null && boxColor.isNotEmpty()) {
                            myView.viewbody_background.background.setColorFilter(Color.parseColor(boxColor), PorterDuff.Mode.SRC_ATOP)
                        }

                        itemView.box_container.addView(myView)
                    }
                    itemView.box_container.visibility = View.VISIBLE
                }
            }


        }

        private fun renderBottomSheet(bottomSheetItem: BottomSheetItem?, position: Int) {

            val closeableBottomSheetDialog: CloseableBottomSheetDialog = CloseableBottomSheetDialog.createInstanceRounded(context)
            val view: View = View.inflate(context, R.layout.mlp_bottomsheet, null)
            view.findViewById<TextView>(R.id.tv_title_bottom).text = bottomSheetItem?.title
            view.findViewById<TextView>(R.id.tv_detail_bottom).text = bottomSheetItem?.text
            val button: Button = view.findViewById(R.id.button_bottomsheet)

            val buttonText: String?
            buttonText = bottomSheetItem?.buttonCta
            button.text = buttonText

            val imageView: ImageView = view.findViewById(R.id.iv_cancel)
            imageView.setOnClickListener {
                closeableBottomSheetDialog.dismiss()
            }
            button.setOnClickListener {

                val url: String? = bottomSheetItem?.url
                url?.let {
                    openWebViewOrOpenBottomSheet(url, position, 3)
                }
            }
            closeableBottomSheetDialog.setContentView(view)
            closeableBottomSheetDialog.show()
            closeableBottomSheetDialog.setCanceledOnTouchOutside(true)

        }


        private fun redirectUrl(urlBody: String?, position: Int) {

            urlBody?.let { url ->

                if (itemView.box_container.isVisible) {
                    itemView.box_container.setOnClickListener {
                        openWebViewOrOpenBottomSheet(url, position, 0)
                    }
                } else if (itemView.info_container.isVisible) {
                    itemView.info_container.setOnClickListener {
                        openWebViewOrOpenBottomSheet(url, position, 0)
                    }
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
                    mlpRouter.startMLPWebViewActivity(context, filteredUrl)

                }
            } else {

                val bottomSheetLength: Int = boxList[position].bottomSheet?.size!!
                val bottomSheetItem = boxList[position].bottomSheet

                when (type) {

                    0 -> {

                        val placeHolder: String? = boxList[position].body?.urlbody
                        val bottomSheetId: Int = computeBottomSheetId(placeHolder, position, bottomSheetLength)

                        if (bottomSheetId >= 0) {
                            renderBottomSheet(bottomSheetItem?.get(bottomSheetId), position)
                        }
                    }

                    1 -> {

                        val placeHolder: String? = boxList[position].header?.sideText?.url
                        val bottomSheetId: Int = computeBottomSheetId(placeHolder, position, bottomSheetLength)

                        if (bottomSheetId >= 0) {
                            renderBottomSheet(bottomSheetItem?.get(bottomSheetId), position)
                        }

                    }

                    2 -> {

                        val placeHolder: String? = boxList[position].header?.sideToggle?.url
                        val bottomSheetId: Int = computeBottomSheetId(placeHolder, position, bottomSheetLength)

                        if (bottomSheetId >= 0) {
                            renderBottomSheet(bottomSheetItem?.get(bottomSheetId), position)
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


    }

}
