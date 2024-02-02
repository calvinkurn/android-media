package com.tokopedia.feedback_form.feedbackpage.domain.model

data class FeedbackModel(
        var categories: List<CategoriesItem> = getDefaultCategories(),
        var labels: List<LabelsItem> = getDefaultLabels()
) {
    companion object {
        fun getDefaultCategories() = mutableListOf<CategoriesItem>().apply {
            this.add(CategoriesItem("Others", 0))
            this.add(CategoriesItem("Crash", 1))
            this.add(CategoriesItem("Design", 2))
            this.add(CategoriesItem("Functionality", 3))
            this.add(CategoriesItem("Tracker", 4))
        }
        fun getDefaultLabels() = mutableListOf<LabelsItem>().apply {
            this.add(LabelsItem(1, "Menunggu Pembayaran", "0"))
            this.add(LabelsItem(2, "Daftar Transaksi", "0"))
            this.add(LabelsItem(3, "Chat", "0"))
            this.add(LabelsItem(4, "Diskusi", "0"))
            this.add(LabelsItem(5, "Ulasan", "0"))
            this.add(LabelsItem(6, "Notifikasi", "0"))
            this.add(LabelsItem(7, "Pengaturan", "0"))
            this.add(LabelsItem(8, "Dana di Tokopedia", "0"))
            this.add(LabelsItem(9, "Kupon Saya", "0"))
            this.add(LabelsItem(10, "Wishlist", "0"))
            this.add(LabelsItem(11, "Feed", "0"))
            this.add(LabelsItem(12, "Official Store", "0"))
            this.add(LabelsItem(13, "PDP", "0"))
            this.add(LabelsItem(14, "Shop Page", "0"))
            this.add(LabelsItem(15, "Pusat Bantuan/Tokopedia Care", "0"))
            this.add(LabelsItem(16, "Penjualan", "0"))
            this.add(LabelsItem(17, "Tambah Produk", "0"))
            this.add(LabelsItem(18, "Produk Anda", "0"))
            this.add(LabelsItem(19, "Pengaturan Power Merchant", "0"))
            this.add(LabelsItem(20, "Pengaturan TopAds", "0"))
            this.add(LabelsItem(21, "Home", "0"))
            this.add(LabelsItem(22, "Search", "0"))
            this.add(LabelsItem(23, "Keranjang", "0"))
            this.add(LabelsItem(24, "Pengiriman / Checkout", "0"))
            this.add(LabelsItem(25, "Pembayaran", "0"))
            this.add(LabelsItem(26, "Others", "0"))
        }
    }
}

data class CategoriesItem(
        var label: String = "",
        var value: Int = -1
)

data class LabelsItem(
        var id: Int = -1,
        var name: String = "",
        var weight: String = "",
        var isSelected: Boolean = false
)
