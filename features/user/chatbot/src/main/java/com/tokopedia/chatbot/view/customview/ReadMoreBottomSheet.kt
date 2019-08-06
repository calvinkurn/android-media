package com.tokopedia.chatbot.view.customview


import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.view.View
import android.widget.*
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.view.fragment.ChatbotFragment


class ReadMoreBottomSheet() : BottomSheetDialogFragment() {

    private var mBehavior: BottomSheetBehavior<FrameLayout>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        val view = View.inflate(context, R.layout.read_more_bottom_sheet, null)
        val tvMsg= view.findViewById<TextView>(R.id.tv_msg)
        val ivClose = view.findViewById<ImageView>(R.id.iv_close_icon)

        var childFragmentList = activity?.supportFragmentManager?.fragments

        childFragmentList?.forEach {
               if(it is ChatbotFragment){
                   tvMsg.movementMethod = ChatLinkHandlerMovementMethod(it as ChatLinkHandlerListener)
                   return@forEach
               }
        }
        tvMsg.text = MethodChecker.fromHtml(arguments?.getString(MESSAGE))
        ivClose.setOnClickListener { this.dismiss() }

        val linearLayout = view.findViewById<LinearLayout>(R.id.read_more_bottom_sheet)
        linearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                Resources.getSystem().displayMetrics.heightPixels)

        dialog.setContentView(view)
        mBehavior = BottomSheetBehavior.from(view.parent as FrameLayout)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        mBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
    }

    companion object {

        const val MESSAGE = "msg"

        @JvmStatic
        fun createInstance(message: String): ReadMoreBottomSheet {
            val args = Bundle()
            args.putString(MESSAGE, message)
            val readMoreBottomSheet = ReadMoreBottomSheet()
            readMoreBottomSheet.arguments=args
            return readMoreBottomSheet
        }
    }
}
