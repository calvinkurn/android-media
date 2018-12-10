package com.tokopedia.chatbot.attachinvoice.domain.model

/**
 * Created by Hendri on 21/03/18.
 */

class Invoice private constructor(var statusInt: Int, var number: String?, var type: String?,
                                  var url: String?, var title: String?, var desc: String?,
                                  var date: String?, var status: String?, var total: String?,
                                  var imageUrl: String?, var invoiceTypeInt: Int,
                                  var invoiceId: Long?) {

    class InvoiceBuilder {
        private var statusInt: Int = 0
        private var number: String? = null
        private var type: String? = null
        private var url: String? = null
        private var title: String? = null
        private var desc: String? = null
        private var date: String? = null
        private var status: String? = null
        private var total: String? = null
        private var imageUrl: String? = null
        private var invoiceTypeInt: Int = 0
        private var invoiceId: Long? = null

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

        fun setInvoiceId(invoiceId: Long?): InvoiceBuilder {
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
