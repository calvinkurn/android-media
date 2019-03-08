package com.tokopedia.contactus.inboxticket2.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{
	@SerializedName("tickets")
	private Tickets tickets;
	@SerializedName("isSuccess")
	private int isSuccess;

	public void setTickets(Tickets tickets){
		this.tickets = tickets;
	}

	public Tickets getTickets(){
		return tickets;
	}


	public void setIsSuccess(int isSuccess){
		this.isSuccess = isSuccess;
	}

	public int getIsSuccess(){
		return isSuccess;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"tickets = '" + tickets + '\'' + 
			",isSuccess = '" + isSuccess + '\'' +
			"}";
		}
}