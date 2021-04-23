package com.tokopedia.buyerorderdetail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.presentation.adapter.ActionButtonClickListener
import com.tokopedia.buyerorderdetail.presentation.adapter.BuyerOrderDetailAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ActionButtonsViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.BuyProtectionViewHolder
import com.tokopedia.buyerorderdetail.presentation.bottomsheet.SecondaryActionButtonBottomSheet
import com.tokopedia.buyerorderdetail.presentation.model.*
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_buyer_order_detail.*
import java.util.*

class BuyerOrderDetailFragment : BaseDaggerFragment(), ActionButtonClickListener, BuyProtectionViewHolder.BuyProtectionListener {

    private val mockModel = BuyerOrderDetailUiModel(
            actionButtons = ActionButtonsUiModel(
                    primaryActionButton = ActionButtonsUiModel.ActionButton(
                            key = "cancel_order",
                            label = "Batalkan Pesanan"
                    ),
                    secondaryActionButtons = listOf(
                            ActionButtonsUiModel.ActionButton(
                                    key = "help",
                                    label = "Bantuan"
                            ),
                            ActionButtonsUiModel.ActionButton(
                                    key = "ask_seller",
                                    label = "Tanya Penjual"
                            )
                    )
            ),
            buyProtectionUiModel = BuyProtectionUiModel(
                    title = "Beli Proteksi?",
                    description = "12 bulan proteksi diluar cakupan garansi resmi, ganti rugi hingga senilai harga barang",
                    deadline = Calendar.getInstance(TimeZone.getDefault()).apply {
                        add(Calendar.SECOND, 15)
                    }.timeInMillis
            ),
            shipmentInfoUiModel = ShipmentInfoUiModel(
                    headerUiModel = PlainHeaderUiModel(
                            header = "Info Pengiriman"
                    ),
                    courierInfoUiModel = ShipmentInfoUiModel.CourierInfoUiModel(
                            courierNameAndProductName = "Same Day - Go Send",
                            isFreeShipping = true,
                            arrivalEstimation = "(Estimasi tiba 17 Jan - 19 Jan)"
                    ),
                    ticker = TickerUiModel(
                            description = "Jaminan tepat waktu. Ongkir kembali jika pesanan tiba lebih dari 15 Dec 20; 15:20 WIB. <a href=\"#\">Lihat S&K</a>"
                    ),
                    courierDriverInfoUiModel = ShipmentInfoUiModel.CourierDriverInfoUiModel(
                            photoUrl = "https://static.wikia.nocookie.net/nickelodeon-movies/images/7/7e/Patrick_Star.png",
                            name = "Patrick Star",
                            phoneNumber = "081234567890",
                            plateNumber = "B 1234 XYZ"
                    ),
                    awbInfoUiModel = ShipmentInfoUiModel.AwbInfoUiModel(
                            awbNumber = "BOOE900142127415"
                    ),
                    receiverReceiverAddressInfoUiModel = ShipmentInfoUiModel.ReceiverAddressInfoUiModel(
                            receiverName = "Spongebob Square Pants",
                            receiverPhoneNumber = "08987654321",
                            receiverAddress = "Tokopedia Tower, Mailing Room Jl. Letjen s parman kav.77 Slipi, Jakarta barat 14410",
                            receiverAddressNote = "Lantai 29"
                    )
            ),
            paymentInfoItems = PaymentInfoUiModel(
                    headerUiModel = PlainHeaderUiModel(
                            header = "Rincian Pembayaran"
                    ),
                    paymentMethodInfoItems = listOf(
                            PaymentInfoUiModel.PaymentInfoItemUiModel(
                                    label = "Metode Pembayaran",
                                    value = "Kartu Kredit"
                            )
                    ),
                    paymentInfoItems = listOf(
                            PaymentInfoUiModel.PaymentInfoItemUiModel(
                                    label = "Total Harga (2 Barang)",
                                    value = "Rp5.000.000"
                            ),
                            PaymentInfoUiModel.PaymentInfoItemUiModel(
                                    label = "Diskon",
                                    value = "- Rp150.000"
                            ),
                            PaymentInfoUiModel.PaymentInfoItemUiModel(
                                    label = "Proteksi Produk",
                                    value = "Rp300.000"
                            ),
                            PaymentInfoUiModel.PaymentInfoItemUiModel(
                                    label = "Jaminan Ready Stock",
                                    value = "Rp500"
                            ),
                            PaymentInfoUiModel.PaymentInfoItemUiModel(
                                    label = "Total Ongkos Kirim (2.20 kg)",
                                    value = "Rp40.000"
                            ),
                            PaymentInfoUiModel.PaymentInfoItemUiModel(
                                    label = "Diskon Ongkos Kirim",
                                    value = "- Rp20.000"
                            ),
                            PaymentInfoUiModel.PaymentInfoItemUiModel(
                                    label = "Asuransi Pengiriman",
                                    value = "Rp3.000"
                            )
                    ),
                    paymentGrandTotal = PaymentInfoUiModel.PaymentGrandTotalUiModel(
                            label = "Total Belanja",
                            value = "Rp5.173.500"
                    ),
                    ticker = TickerUiModel("Cashback <b>30.000 OVO Points</b>")
            ),
            productListUiModel = ProductListUiModel(
                    productListHeaderUiModel = ProductListUiModel.ProductListHeaderUiModel(
                            header = "Detail Produk",
                            shopBadge = 2,
                            shopName = "Rockerpower Shopz"
                    ),
                    productList = listOf(
                            ProductListUiModel.ProductUiModel(
                                    productThumbnailUrl = "https://i.pinimg.com/originals/18/e6/96/18e696b711dbb20adf11ecf8ff2ce0ec.png",
                                    productName = "Patrick Star Bertapa",
                                    productQuantityAndPrice = "2 x Rp 2.000.000",
                                    productNote = "43 Size. Packing rapi plis.",
                                    totalPrice = "Rp 4.000.000",
                                    showBuyAgainButton = true,
                                    showClaimInsurance = true
                            ),
                            ProductListUiModel.ProductUiModel(
                                    productThumbnailUrl = "https://akcdn.detik.net.id/visual/2020/08/11/patrick-star-di-spongebob-squarepants_169.jpeg?w=650",
                                    productName = "Patrick Star Makan Krabby Patty Pake Helm Sandy yang dipenuhi dengan Air Laut yang Asin (Menyelam sambil minum air laut)",
                                    productQuantityAndPrice = "4 x Rp 5.000.000",
                                    productNote = "43 Size. Packing rapi plis pake bubble wrap yg tebel, kalau ga tebel nanti saya minta refund karena ga tebel bubble wrapnya, kasian patricknya.",
                                    totalPrice = "Rp 20.000.000",
                                    showBuyAgainButton = false,
                                    showClaimInsurance = true
                            ),
                            ProductListUiModel.ProductUiModel(
                                    productThumbnailUrl = "https://img.tek.id/crop/330x230/content/2020/08/11/31957/nickelodeon-garap-serial-tv-spin-off-patrick-6siywPEb7g.jpg",
                                    productName = "Patrick Star Vacuum Cleaner",
                                    productQuantityAndPrice = "6 x Rp 5.000.000",
                                    productNote = "43 Size. Packing rapi plis.",
                                    totalPrice = "Rp 30.000.000",
                                    showBuyAgainButton = false,
                                    showClaimInsurance = false
                            ),
                            ProductListUiModel.ProductUiModel(
                                    productThumbnailUrl = "https://www.greenscene.co.id/wp-content/uploads/2020/08/Patrick-Stars-696x497.jpg",
                                    productName = "Telepon Patrick Star",
                                    productQuantityAndPrice = "8 x Rp 5.000.000",
                                    productNote = "43 Size. Packing rapi plis.",
                                    totalPrice = "Rp 40.000.000",
                                    showBuyAgainButton = true,
                                    showClaimInsurance = false
                            ),
                            ProductListUiModel.ProductUiModel(
                                    productThumbnailUrl = "https://cdn.kincir.com/1/production/media/2018/april/silsilah-keluarga-patrick-star-yang-harus-lo-tahu/2-patrick-patar-700x700.jpg",
                                    productName = "Patrick Star Muda",
                                    productQuantityAndPrice = "10 x Rp 5.000.000",
                                    productNote = "",
                                    totalPrice = "Rp 50.000.000",
                                    showBuyAgainButton = false,
                                    showClaimInsurance = false
                            )
                    )
            ),
            orderStatusUiModel = OrderStatusUiModel(
                    orderStatusHeaderUiModel = OrderStatusUiModel.OrderStatusHeaderUiModel(
                            indicatorColor = "#FFC400",
                            orderStatus = "Menunggu Konfirmasi"
                    ),
                    ticker = TickerUiModel(
                            description = "Pengajuan pembatalan berhasil dan menunggu persetujuan dari penjual. <a href=\"#\">Lihat S&K</a>"
                    ),
                    orderStatusInfoUiModel = OrderStatusUiModel.OrderStatusInfoUiModel(
                            invoice = "INV/20161025/XVI/X/55069657",
                            purchaseDate = "25 Des 2021, 05:00 WIB",
                            deadline = Calendar.getInstance(TimeZone.getDefault()).apply {
                                add(Calendar.HOUR, 10)
                            }.timeInMillis
                    )
            ))

    private val typeFactory: BuyerOrderDetailTypeFactory by lazy {
        BuyerOrderDetailTypeFactory(this, this)
    }
    private val adapter: BuyerOrderDetailAdapter by lazy {
        BuyerOrderDetailAdapter(typeFactory)
    }
    private val secondaryActionButtonBottomSheet: SecondaryActionButtonBottomSheet by lazy {
        SecondaryActionButtonBottomSheet(requireContext(), this)
    }

    override fun getScreenName() = BuyerOrderDetailFragment::class.java.simpleName

    override fun initInjector() {
        getComponent(BuyerOrderDetailComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_buyer_order_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun onActionButtonClicked(key: String) {
        when (key) {
            ActionButtonsViewHolder.SECONDARY_ACTION_BUTTON_KEY -> onSecondaryActionButtonClicked()
            else -> {
                view?.let {
                    Toaster.build(it, key, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
                }
            }
        }
    }

    override fun onClickBuyProtection() {
        view?.let {
            Toaster.build(it, "Beli proteksi dong...", Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        }
    }

    override fun onReachBuyProtectionDeadline() {
//        adapter.removeBuyProtectionWidget()
    }

    private fun setupViews() {
        setupToolbar()
        setupRecyclerView()
    }

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.apply {
            supportActionBar?.hide()
            setSupportActionBar(toolbarBuyerOrderDetail)
        }
    }

    private fun setupRecyclerView() {
        rvBuyerOrderDetail.adapter = adapter
        // status
        addOrderStatusHeaderSection()
        addTickerSection(mockModel.orderStatusUiModel.ticker)
        addThinDividerSection()
        addOrderStatusInfoSection()
        // product list
        addThickDividerSection()
        addProductListHeaderSection()
        addProductListSection()
        // buy protection
        addThickDividerSection()
        addBuyProtectionSection()
        // shipment info
        addThickDividerSection()
        addPlainHeaderSection(mockModel.shipmentInfoUiModel.headerUiModel)
        addTickerSection(mockModel.shipmentInfoUiModel.ticker)
        addCourierInfoSection()
        addThinDashedDividerSection()
        addCourierDriverInfoSection()
        addThinDashedDividerSection()
        addAwbInfoSection()
        addReceiverAddressInfoSection()
        // payment info
        addThickDividerSection()
        addPlainHeaderSection(mockModel.paymentInfoItems.headerUiModel)
        addPaymentMethodSection()
        addThinDividerSection()
        addPaymentInfoSection()
        addThinDividerSection()
        addPaymentGrandTotalSection()
        addTickerSection(mockModel.paymentInfoItems.ticker)
        // action buttons
        addActionButtonsSection()
    }

    private fun addOrderStatusInfoSection() {
        adapter.addElement(mockModel.orderStatusUiModel.orderStatusInfoUiModel)
    }

    private fun addOrderStatusHeaderSection() {
        adapter.addElement(mockModel.orderStatusUiModel.orderStatusHeaderUiModel)
    }

    private fun addProductListSection() {
        mockModel.productListUiModel.productList.forEach {
            adapter.addElement(it)
        }
    }

    private fun addProductListHeaderSection() {
        adapter.addElement(mockModel.productListUiModel.productListHeaderUiModel)
    }

    private fun addReceiverAddressInfoSection() {
        adapter.addElement(mockModel.shipmentInfoUiModel.receiverReceiverAddressInfoUiModel)
    }

    private fun addAwbInfoSection() {
        adapter.addElement(mockModel.shipmentInfoUiModel.awbInfoUiModel)
    }

    private fun addCourierInfoSection() {
        adapter.addElement(mockModel.shipmentInfoUiModel.courierInfoUiModel)
    }

    private fun addPlainHeaderSection(headerUiModel: PlainHeaderUiModel) {
        adapter.addElement(headerUiModel)
    }

    private fun addThinDashedDividerSection() {
        adapter.addElement(ThinDashedDividerUiModel())
    }

    private fun addThickDividerSection() {
        adapter.addElement(ThickDividerUiModel())
    }

    private fun addCourierDriverInfoSection() {
        adapter.addElement(mockModel.shipmentInfoUiModel.courierDriverInfoUiModel)
    }

    private fun addTickerSection(tickerUiModel: TickerUiModel) {
        adapter.addElement(tickerUiModel)
    }

    private fun addBuyProtectionSection() {
        adapter.addElement(mockModel.buyProtectionUiModel)
    }

    private fun addPaymentMethodSection() {
        mockModel.paymentInfoItems.paymentMethodInfoItems.forEach {
            adapter.addElement(it)
        }
    }

    private fun addThinDividerSection() {
        adapter.addElement(ThinDividerUiModel())
    }

    private fun addPaymentInfoSection() {
        mockModel.paymentInfoItems.paymentInfoItems.forEach {
            adapter.addElement(it)
        }
    }

    private fun addPaymentGrandTotalSection() {
        adapter.addElement(mockModel.paymentInfoItems.paymentGrandTotal)
    }

    private fun addActionButtonsSection() {
        adapter.addElement(mockModel.actionButtons)
    }

    private fun onSecondaryActionButtonClicked() {
        secondaryActionButtonBottomSheet.setSecondaryActionButtons(mockModel.actionButtons.secondaryActionButtons)
        secondaryActionButtonBottomSheet.show(childFragmentManager)
    }
}