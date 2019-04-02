package com.tokopedia.contactus.orderquery.data;

import com.google.gson.annotations.SerializedName;

public class Order{

	@SerializedName("id")
	private int id;

	@SerializedName("invoice")
	private String invoice;

	@SerializedName("product_type_id")
	private int productTypeId;

	@SerializedName("invoice_type_id")
	private int invoiceTypeId;

	@SerializedName("status")
	private int status;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setInvoice(String invoice){
		this.invoice = invoice;
	}

	public String getInvoice(){
		return invoice;
	}

	public void setProductTypeId(int productTypeId){
		this.productTypeId = productTypeId;
	}

	public int getProductTypeId(){
		return productTypeId;
	}

	public void setInvoiceTypeId(int invoiceTypeId){
		this.invoiceTypeId = invoiceTypeId;
	}

	public int getInvoiceTypeId(){
		return invoiceTypeId;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"Order{" + 
			"id = '" + id + '\'' + 
			",invoice = '" + invoice + '\'' + 
			",product_type_id = '" + productTypeId + '\'' + 
			",invoice_type_id = '" + invoiceTypeId + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}