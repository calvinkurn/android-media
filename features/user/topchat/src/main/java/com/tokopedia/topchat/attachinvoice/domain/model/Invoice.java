package com.tokopedia.topchat.attachinvoice.domain.model;

/**
 * Created by Hendri on 21/03/18.
 */

public class Invoice {
    int statusInt;
    String number;
    String type;
    String url;
    String title;
    String desc;
    String date;
    String status;
    String total;
    String imageUrl;
    int invoiceTypeInt;
    Long invoiceId;

    private Invoice(int statusInt, String number, String type, String url, String title, String
            desc, String date, String status, String total, String imageUrl, int invoiceTypeInt,
                   Long invoiceId) {
        this.statusInt = statusInt;
        this.number = number;
        this.type = type;
        this.url = url;
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.status = status;
        this.total = total;
        this.imageUrl = imageUrl;
        this.invoiceTypeInt = invoiceTypeInt;
        this.invoiceId = invoiceId;
    }

    public int getStatusInt() {
        return statusInt;
    }

    public void setStatusInt(int statusInt) {
        this.statusInt = statusInt;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getInvoiceTypeInt() {
        return invoiceTypeInt;
    }

    public void setInvoiceTypeInt(int invoiceTypeInt) {
        this.invoiceTypeInt = invoiceTypeInt;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public static class InvoiceBuilder {
        private int statusInt;
        private String number;
        private String type;
        private String url;
        private String title;
        private String desc;
        private String date;
        private String status;
        private String total;
        private String imageUrl;
        private int invoiceTypeInt;
        private Long invoiceId;

        public static InvoiceBuilder getInstance(){
            return new InvoiceBuilder();
        }

        public InvoiceBuilder setStatusInt(int statusInt) {
            this.statusInt = statusInt;
            return this;
        }

        public InvoiceBuilder setNumber(String number) {
            this.number = number;
            return this;
        }

        public InvoiceBuilder setType(String type) {
            this.type = type;
            return this;
        }

        public InvoiceBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public InvoiceBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public InvoiceBuilder setDesc(String desc) {
            this.desc = desc;
            return this;
        }

        public InvoiceBuilder setDate(String date) {
            this.date = date;
            return this;
        }

        public InvoiceBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public InvoiceBuilder setTotal(String total) {
            this.total = total;
            return this;
        }

        public InvoiceBuilder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public InvoiceBuilder setInvoiceTypeInt(int invoiceTypeInt) {
            this.invoiceTypeInt = invoiceTypeInt;
            return this;
        }

        public InvoiceBuilder setInvoiceId(Long invoiceId) {
            this.invoiceId = invoiceId;
            return this;
        }

        public Invoice createInvoice() {
            return new Invoice(statusInt,
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
                    invoiceId);
        }
    }
}
