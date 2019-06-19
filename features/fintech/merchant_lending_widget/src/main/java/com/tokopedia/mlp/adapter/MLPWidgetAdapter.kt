package com.tokopedia.mlp.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.*
import com.example.merchant_lending_widget.R
import com.tokopedia.abstraction.common.utils.HexValidator
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.mlp.contractModel.*
import kotlinx.android.synthetic.main.fragment_merchant_lending.view.*
import kotlinx.android.synthetic.main.mlp_bottomsheet.view.*
import kotlinx.android.synthetic.main.mlp_box_layout.view.*
import kotlinx.android.synthetic.main.mlp_row_info.view.*


class MLPWidgetAdapter(private val boxList: List<WidgetsItem>, val context: Context) : RecyclerView.Adapter<MLPWidgetAdapter.ViewHolder>(), View.OnClickListener {


    override fun onClick(v: View?) {
        Toast.makeText(context, "Adapter", Toast.LENGTH_SHORT).show()

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MLPWidgetAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_merchant_lending, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(boxList[position], position)
    }

    override fun getItemCount(): Int {
        return if (boxList == null) 0 else boxList.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(widgetsItem: WidgetsItem, position: Int) {
            widgetsItem.header?.let {
                itemView.text_title.setTextAndCheckShow(it.title)

                if(it.logo!=null) {
                    itemView.header_logo.visibility = View.VISIBLE
                    ImageHandler.loadImage(context, itemView.header_logo, it.logo, R.color.grey_100)
                }
                itemView.text_badge.setTextAndCheckShow(it.tag?.name)

                if (HexValidator.validate(it.tag?.color)) {
                    itemView.text_badge.background.setColorFilter(Color.parseColor(it.tag?.color), PorterDuff.Mode.SRC_ATOP)
                }

                if (it.sideToggle != null && it.sideText != null) {
                    renderSideTextHeader(it, position)
                } else if (it.sideToggle != null && it.sideText == null) {
                    renderSideToggleHeader(it)
                } else if (it.sideText != null && it.sideToggle == null)
                    renderSideTextHeader(it, position)

            }

            widgetsItem.body?.let {
                renderBodyInfo(it.info)
                renderBodyBox(it.boxes)
                redirectUrl(it.urlbody, position)
            }

            /*    widgetsItem.body?.let {
                     renderBodyInfo(it)
                     renderBodyBox(it)
                 }*/
        }

        private fun redirectUrl(urlbody: String?, position: Int) {

            if (urlbody != null) {
                itemView.box_container.setOnClickListener {
                    if (URLUtil.isValidUrl(urlbody)) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlbody))
                        context.startActivity(intent)
                    } else {
                        checkBottomSheetID(position, 1)
                    }
                }
            }


        }

        private fun renderBottomSheet(bottomSheetItem: List<BottomSheetItem?>?, res: Int) {


            var bottomsheetitem: BottomSheetItem = bottomSheetItem?.get(res)!!

            var closeableBottomSheetDialog: CloseableBottomSheetDialog = CloseableBottomSheetDialog.createInstanceRounded(context)
            var view: View = View.inflate(context, R.layout.mlp_bottomsheet, null)
            view.findViewById<TextView>(R.id.tv_title_bottom).text = bottomsheetitem?.title
            view.findViewById<TextView>(R.id.tv_detail_bottom).text = bottomsheetitem?.text

            var buttonText: String? = null

            buttonText = bottomsheetitem?.buttonCta

            view.findViewById<TextView>(R.id.button_bottomsheet).text = buttonText

            itemView.button_bottomsheet.setOnClickListener {
                if (URLUtil.isValidUrl(bottomsheetitem?.url)) {

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(bottomsheetitem?.url))
                    context.startActivity(intent)
                }
            }

            closeableBottomSheetDialog.setContentView(view)
            closeableBottomSheetDialog.show()
            closeableBottomSheetDialog.setCanceledOnTouchOutside(true)


        }

        private fun renderBodyBox(it: List<BoxesItem?>?) {


            if (it == null) {
                itemView.box_container.visibility = View.GONE

            } else {
                var boxTitle: String? = null
                var boxContent: String? = null
                var boxColor: String? = null
                var boxUrl: String? = null
                var length: Int = it.size


                for (item in 0..length - 1) {

                    if (it[item]?.title?.length!! > 0 || it[item]?.text?.length!! > 0) {
                        boxTitle = it.get(item)?.title
                        boxContent = it.get(item)?.text
                        boxColor = it.get(item)?.boxColor
                        itemView.box_container.visibility=View.VISIBLE

                        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                        val myView = inflater.inflate(R.layout.mlp_box_layout, null)

                        myView.text_body_title.setTextAndCheckShow(boxTitle)
                        myView.text_body_content.setTextAndCheckShow(boxContent)
                        myView.viewbody_background.visibility=View.VISIBLE

                        if (boxColor != null && boxColor.isNotEmpty()) {
                            myView.viewbody_background.background.setColorFilter(Color.parseColor(boxColor), PorterDuff.Mode.SRC_ATOP)
                        }

                        itemView.box_container.addView(myView)
                    }
                }
            }


        }


        private fun renderBodyInfo(it: List<InfoItem?>?) {

            if (it == null) {
                itemView.info_container.visibility = View.GONE

            } else {
                itemView.info_container.visibility = View.VISIBLE
                var infolabel: String? = null
                var infovalue: String? = null
                var length: Int = it.size


                for (item in 0..length - 1) {
                    infolabel = it.get(item)?.label
                    infovalue = it.get(item)?.value

                    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val myView = inflater.inflate(R.layout.mlp_row_info, null)

                    myView.label.setTextAndCheckShow(infolabel)
                    myView.value.setTextAndCheckShow(infovalue)

                    itemView.info_container.addView(myView)
                }

            }

        }

        private fun renderSideToggleHeader(headerContent: Header) {


            itemView.flip_switch.visibility = View.VISIBLE
            itemView.flip_switch.displayedChild = 0

            var showToggle: Boolean? = null
            var statusToggle: Boolean? = null

            if (headerContent.sideToggle?.showToggle!!) {
                showToggle = headerContent.sideToggle.showToggle

            }
            if (showToggle == true) {
                if (headerContent.sideToggle.toggleStatus!!) {
                    statusToggle = headerContent.sideToggle.toggleStatus

                    itemView.switch_enable.visibility = View.VISIBLE
                    if (statusToggle == false) {
                        itemView.switch_enable.toggle()
                    } else
                        itemView.switch_enable.isChecked = false

                }
            }

        }

        private fun renderSideTextHeader(headerContent: Header, position: Int) {

            itemView.flip_switch.visibility = View.VISIBLE
            itemView.flip_switch.displayedChild = 1
            itemView.text_side.setTextAndCheckShow(headerContent.sideText?.text)



            itemView.text_side.setOnClickListener {
                if (URLUtil.isValidUrl(headerContent.sideText?.url)) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(headerContent.sideText?.url))
                    context.startActivity(intent)


                } else {
                    itemView.text_side.tag = position
                    val position = it.tag as Int
                    var res: Int = checkBottomSheetID(position, 0)
                    if (res > 0) {
                        Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
                        renderBottomSheet(boxList[position].bottomSheet, res)
                    }


                }
            }
        }


        private fun checkBottomSheetID(position: Int, type: Int): Int {

            if (type == 0) {


                var bottomsheetLength: Int = boxList[position].bottomSheet?.size!!
                var index: Int = -1
                for (bottomItem in 0..bottomsheetLength - 1) {
                    index++
                    if (boxList[position].header?.sideText?.url?.equals(boxList[position].bottomSheet!![bottomItem]?.id)!!) {
                        return index
                    }

                }

            } else if (type == 1) {

                var index1: Int = -1
                var bottomsheetLength1: Int = boxList[position].bottomSheet?.size!!
                for (bottomItem in 0..bottomsheetLength1 - 1) {
                    index1++
                    if (boxList[position].body?.urlbody.equals(boxList[position].bottomSheet!![bottomItem]?.id)) {
                        return index1
                    }

                }
            }
            return -1
        }
    }

}

