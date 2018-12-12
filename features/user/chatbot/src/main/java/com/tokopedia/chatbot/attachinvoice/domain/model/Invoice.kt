package com.tokopedia.chatbot.attachinvoice.domain.model

/**
 * Created by Hendri on 21/03/18.
 */

class Invoice private constructor(var statusInt: Int,
                                  var number: String = "",
                                  var type: String = "",
                                  var url: String = "",
                                  var title: String = "",
                                  var desc: String = "",
                                  var date: String = "",
                                  var status: String = "",
                                  var total: String = "",
                                  var imageUrl: String = "",
                                  var invoiceTypeInt: Int,
                                  var invoiceId: Long = 0) {

    class InvoiceBuilder {
        private var statusInt: Int = 0
        private var number: String = ""
        private var type: String = ""
        private var url: String = ""
        private var title: String = ""
        private var desc: String = ""
        private var date: String = ""
        private var status: String = ""
        private var total: String = ""
        private var imageUrl: String = ""
        private var invoiceTypeInt: Int = 0
        private var invoiceId: Long = 0

        fun setStatusInt(statusInt: Int): InvoiceBuilder {
            this.statusInt = statusInt
            return this
        }

        fun setNumber(number: String): InvoiceBuilder {
            this.number = number
            return this
        }

        fun setType(type: String): InvoiceBuilder {
            this.type = type
            return this
        }

        fun setUrl(url: String): InvoiceBuilder {
            this.url = url
            return this
        }

        fun setTitle(title: String): InvoiceBuilder {
            this.title = title
            return this
        }

        fun setDesc(desc: String): InvoiceBuilder {
            this.desc = desc
            return this
        }

        fun setDate(date: String): InvoiceBuilder {
            this.date = date
            return this
        }

        fun setStatus(status: String): InvoiceBuilder {
            this.status = status
            return this
        }

        fun setTotal(total: String): InvoiceBuilder {
            this.total = total
            return this
        }

        fun setImageUrl(imageUrl: String): InvoiceBuilder {
            this.imageUrl = imageUrl
            return this
        }

        fun setInvoiceTypeInt(invoiceTypeInt: Int): InvoiceBuilder {
            this.invoiceTypeInt = invoiceTypeInt
            return this
        }

        fun setInvoiceId(invoiceId: Long): InvoiceBuilder {
            this.invoiceId = invoiceId
            return this
        }

        fun createInvoice(): Invoice {
            return Invoice(statusInt,
                    number,
                    type,
                    url,
                    title,
                    desc,
                    date,
                    status,
                    total,
                    imageUrl,
                    invoiceTypeInt,
                    invoiceId)
        }

        companion object {

            val instance: InvoiceBuilder
                get() = InvoiceBuilder()
        }
    }
}
