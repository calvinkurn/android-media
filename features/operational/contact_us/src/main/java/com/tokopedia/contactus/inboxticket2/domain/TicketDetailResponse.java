package com.tokopedia.contactus.inboxticket2.domain;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

public class TicketDetailResponse{

	@SerializedName("tickets")
	private Tickets tickets;

	@SerializedName("is_success")
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
}