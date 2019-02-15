package com.tokopedia.contactus.inboxticket2.data.model;

import java.util.List;

public class Data{
	private Tickets tickets;
	private List<Object> messageError;
	private int isSuccess;

	public void setTickets(Tickets tickets){
		this.tickets = tickets;
	}

	public Tickets getTickets(){
		return tickets;
	}

	public void setMessageError(List<Object> messageError){
		this.messageError = messageError;
	}

	public List<Object> getMessageError(){
		return messageError;
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
			",messageError = '" + messageError + '\'' + 
			",isSuccess = '" + isSuccess + '\'' + 
			"}";
		}
}