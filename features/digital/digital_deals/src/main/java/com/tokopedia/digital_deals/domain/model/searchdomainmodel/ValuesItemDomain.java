package com.tokopedia.digital_deals.domain.model.searchdomainmodel;

import com.google.gson.annotations.SerializedName;

public class ValuesItemDomain {

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("priority")
	private int priority;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setPriority(int priority){
		this.priority = priority;
	}

	public int getPriority(){
		return priority;
	}

	@Override
	public String toString(){
		return
				"ValuesItemDomain{" +
						"name = '" + name + '\'' +
						",id = '" + id + '\'' +
						",priority = '" + priority + '\'' +
						"}";
	}
}