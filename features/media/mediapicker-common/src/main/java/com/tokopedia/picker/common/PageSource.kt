package com.tokopedia.picker.common

enum class PageSource(val value: String) {
    AddEditProduct("Add Edit Product"),
    AddProduct("Add Product"),
    EditProduct("Edit Product"),
    AddVariant("Add Variant"),
    EditVariant("Edit Variant"),
    Feed("Feed"),
    ChatBot("Chat Bot"),
    TopChat("Top Chat"),
    Play("Play"),
    Review("Review"),
    Feedback("Feedback"),
    CreatePost("Create Post"),
    ProductReport("Product Report"),
    SellerFeedback("Seller Feedback"),
    ShopEditInfo("Shop Edit Info"),
    CreateTicketForm("Create Ticker Form"),
    Inbox("Inbox Detail"),
    AccountDocumentSettingBank("Account Document Setting Bank"),
    ProofPayment("Upload Proof Payment"),
    FlightCancellationReason("Flight Cancellation Reason"),
    Epharmacy("Epharmacy"),
    PlayShorts("Play Shorts"),
    AddLogo("Add Logo"),
    Unknown("");

    companion object {
        private val map = values().associateBy(PageSource::value)

        fun fromString(value: String): PageSource {
            return map[value] ?: Unknown
        }
    }
}
