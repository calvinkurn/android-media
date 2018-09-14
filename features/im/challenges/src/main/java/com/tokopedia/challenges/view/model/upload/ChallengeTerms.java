package com.tokopedia.challenges.view.model.upload;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class ChallengeTerms{

	@SerializedName("Terms")
	private String terms;

	@SerializedName("Collection")
	private Collection collection;

	public void setTerms(String terms){
		this.terms = terms;
	}

	public String getTerms(){
		return terms;
	}

	public void setCollection(Collection collection){
		this.collection = collection;
	}

	public Collection getCollection(){
		return collection;
	}

	@Override
 	public String toString(){
		return 
			"ChallengeTerms{" + 
			"terms = '" + terms + '\'' + 
			",collection = '" + collection + '\'' + 
			"}";
		}
}