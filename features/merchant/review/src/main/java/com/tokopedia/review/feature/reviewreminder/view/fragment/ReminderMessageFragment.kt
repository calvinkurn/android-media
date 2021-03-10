package com.tokopedia.review.feature.reviewreminder.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.review.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class ReminderMessageFragment : Fragment() {

    companion object {
        private const val TAG_BOTTOM_SHEET_HOW_TO = "bottomSheetHowTo"
        private const val TAG_BOTTOM_SHEET_EDIT_MESSAGE = "bottomSheetEditMessage"
    }

    private var iconInformation: IconUnify? = null
    private var buttonEditMessage: UnifyButton? = null
    private var textSampleMessage: Typography? = null
    private var textEstimation: Typography? = null
    private var buttonSend: UnifyButton? = null
    private var rvProducts: RecyclerView? = null
    private var scrollView: ScrollView? = null
    private var cardProducts: CardUnify? = null

    private var bottomSheetHowTo: BottomSheetUnify? = null
    private var bottomSheetEditMessage: BottomSheetUnify? = null
    private var dialogSend: DialogUnify? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reminder_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iconInformation = view.findViewById(R.id.icon_information)
        buttonEditMessage = view.findViewById(R.id.button_edit_message)
        textSampleMessage = view.findViewById(R.id.text_sample_message)
        textEstimation = view.findViewById(R.id.text_estimation)
        buttonSend = view.findViewById(R.id.button_send)
        rvProducts = view.findViewById(R.id.rv_products)
        scrollView = view.findViewById(R.id.scroll_view)
        cardProducts = view.findViewById(R.id.card_products)

        initBottomSheet()
        initView()
        setupViewInteraction()
        initCoachMark()
    }

    private fun initBottomSheet() {
        bottomSheetHowTo = BottomSheetUnify()
        bottomSheetHowTo?.setTitle("Cara pakai pengingat ulasan")
        bottomSheetHowTo?.setChild(View.inflate(context, R.layout.bottom_sheet_review_reminder_how_to, null))

        bottomSheetEditMessage = BottomSheetUnify()
        bottomSheetEditMessage?.setTitle("Ubah Pesan")
        bottomSheetEditMessage?.setChild(View.inflate(context, R.layout.bottom_sheet_review_reminder_edit_message, null))
        bottomSheetEditMessage?.isKeyboardOverlap = false
    }

    private fun initView() {
        textSampleMessage?.text = "Hi Mia, makasih sudah belanja di toko kami. Bisa bantu kasih ulasannya? \uD83D\uDE00"
        textEstimation?.text = "Estimasi terkirim ke 26 pembeli (10 produk)"

        dialogSend = DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            dialogTitle.text = "Kirim Pengingat Ulasan ini?"
            setPrimaryCTAText("Kirim")
            setSecondaryCTAText("Ubah Pesan")
        }
    }

    private fun setupViewInteraction() {
        iconInformation?.setOnClickListener {
            bottomSheetHowTo?.show(childFragmentManager, TAG_BOTTOM_SHEET_HOW_TO)
        }

        buttonEditMessage?.setOnClickListener {
            bottomSheetEditMessage?.show(childFragmentManager, TAG_BOTTOM_SHEET_EDIT_MESSAGE)
        }

        buttonSend?.setOnClickListener { dialogSend?.show() }
    }

    private fun initCoachMark() {

        val coachMarkItems = arrayListOf(
                CoachMark2Item(
                        textSampleMessage as View,
                        "Ingatkan pembeli untuk kasih ulasan melalui chat",
                        "Pesan hanya bisa dikirim min. 7 hari setelah transaksi selesai ke pembeli yang belum tulis ulasan dan pernah chat sebelumnya."
                ),
                CoachMark2Item(
                        buttonEditMessage as View,
                        "Ubah pesan chat di sini",
                        "Kamu dapat mengubah isi pesan sesuai dengan keinginanmu."
                ),
                CoachMark2Item(
                        cardProducts as View,
                        "Daftar produk yang belum diulas",
                        "Cek produk apa saja yang belum mendapat ulasan di sini."
                )
        )
        val coachMark = CoachMark2(requireContext())
        coachMark.showCoachMark(coachMarkItems, scrollView)
    }
}