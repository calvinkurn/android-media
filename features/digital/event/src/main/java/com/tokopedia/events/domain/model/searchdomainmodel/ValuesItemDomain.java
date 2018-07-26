package com.tokopedia.events.domain.model.searchdomainmodel;

import com.google.gson.annotations.SerializedName;

public class ValuesItemDomain {

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;

	private boolean isSelected;

    private boolean multi;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setIsSelected(boolean selected){
		this.isSelected = selected;
	}

	public boolean getIsSelected(){
		return isSelected;
	}

    public boolean isMulti() {
        return multi;
    }

    public void setMulti(boolean multi) {
        this.multi = multi;
    }

	@Override
 	public String toString(){
		return 
			"ValuesItemDomain{" +
			"name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			",isSelected = '" + isSelected + '\'' +
			"}";
		}
}