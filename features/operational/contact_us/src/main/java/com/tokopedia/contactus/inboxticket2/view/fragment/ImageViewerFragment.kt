package com.tokopedia.contactus.inboxticket2.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.widget.TouchViewPager
import com.tokopedia.contactus.R
import com.tokopedia.contactus.common.analytics.ContactUsTracking
import com.tokopedia.contactus.common.analytics.InboxTicketTracking
import com.tokopedia.design.list.adapter.TouchImageAdapter
import java.util.*

class ImageViewerFragment : Fragment() {
    private var vpImageViewer: TouchViewPager? = null
    private var closeButton: ImageView? = null
    private var imageLoc: ArrayList<String>? = null
    private var scrollPos = 0
    fun setImageData(position: Int, image_loc: ArrayList<String>) {
        scrollPos = position
        imageLoc = image_loc
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scrollPos = arguments?.getInt(SCROLL_POSITION) ?: 0
        imageLoc = arguments?.getStringArrayList(IMAGE_ARRAY)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.contactus_layout_fragment_image_viewer, container, false)
        vpImageViewer = contentView.findViewById(R.id.vp_image_viewer)
        closeButton = contentView.findViewById(R.id.close_button)
        val imageAdapter = TouchImageAdapter(context, imageLoc)
        vpImageViewer?.adapter = imageAdapter
        imageAdapter.SetonImageStateChangeListener(object : TouchImageAdapter.OnImageStateChange {
            override fun OnStateZoom() {
                vpImageViewer?.setAllowPageSwitching(false)
            }

            override fun OnStateDefault() {
                vpImageViewer?.setAllowPageSwitching(true)
            }
        })
        closeButton?.setOnClickListener { closeFragment() }
        return contentView
    }

    private fun closeFragment() {
        if (activity != null) activity?.supportFragmentManager?.popBackStack()
    }

    override fun onResume() {
        super.onResume()
        if (activity != null) {
            val actionBar = (activity as AppCompatActivity?)?.supportActionBar
            actionBar?.hide()
        }
        vpImageViewer?.currentItem = scrollPos
        ContactUsTracking.sendGTMInboxTicket(context, "",
                InboxTicketTracking.Category.EventInboxTicket,
                InboxTicketTracking.Action.EventClickOpenImage,
                "")
    }

    override fun onStop() {
        super.onStop()
        if (activity != null) {
            val actionBar = (activity as AppCompatActivity?)?.supportActionBar
            actionBar?.show()
        }
    }

    companion object {
        private const val IMAGE_ARRAY = "image_array"
        private const val SCROLL_POSITION = "scroll_position"
        const val TAG = "ImageViewerFragment"

        @JvmStatic
        fun newInstance(postion: Int, image_loc: ArrayList<String>): ImageViewerFragment {
            val fragment = ImageViewerFragment()
            val args = Bundle()
            args.putInt(SCROLL_POSITION, postion)
            args.putStringArrayList(IMAGE_ARRAY, image_loc)
            fragment.arguments = args
            return fragment
        }
    }
}