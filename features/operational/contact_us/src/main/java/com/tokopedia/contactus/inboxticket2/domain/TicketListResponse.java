package com.tokopedia.contactus.inboxticket2.domain;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

public class TicketListResponse{

	@SerializedName("next_page")
	private String nextPage;

	@SerializedName("tickets")
	private List<TicketsItem> tickets;

	@SerializedName("previous_page")
	private String previousPage;

	@SerializedName("is_success")
	private int isSuccess;

	public void setNextPage(String nextPage){
		this.nextPage = nextPage;
	}

	public String getNextPage(){
		return nextPage;
	}

	public void setTickets(List<TicketsItem> tickets){
		this.tickets = tickets;
	}

	public List<TicketsItem> getTickets(){
		return tickets;
	}

	public void setPreviousPage(String previousPage){
		this.previousPage = previousPage;
	}

	public String getPreviousPage(){
		return previousPage;
	}

	public void setIsSuccess(int isSuccess){
		this.isSuccess = isSuccess;
	}

	public int getIsSuccess(){
		return isSuccess;
	}
}