package com.tokopedia.withdraw.domain.model.validatePopUp;

import com.google.gson.annotations.SerializedName;

public class ValidatePopUpWithdrawal{

	@SerializedName("data")
	private Data data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}



	public class Data{

		@SerializedName("note")
		private String note;

		@SerializedName("needShow")
		private boolean needShow;

		@SerializedName("title")
		private String title;

		public void setNote(String note){
			this.note = note;
		}

		public String getNote(){
			return note;
		}

		public void setNeedShow(boolean needShow){
			this.needShow = needShow;
		}

		public boolean isNeedShow(){
			return needShow;
		}

		public void setTitle(String title){
			this.title = title;
		}

		public String getTitle(){
			return title;
		}
	}
}

