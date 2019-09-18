package com.tokopedia.discovery.categoryrevamp.data.bannedCategory;

import com.google.gson.annotations.SerializedName;

public class CategoryDetailQueryV3{

	@SerializedName("data")
	private Data data;

	@SerializedName("header")
	private Header header;

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	public void setHeader(Header header){
		this.header = header;
	}

	public Header getHeader(){
		return header;
	}

	@Override
 	public String toString(){
		return 
			"CategoryDetailQueryV3{" + 
			"data = '" + data + '\'' + 
			",header = '" + header + '\'' + 
			"}";
		}
}