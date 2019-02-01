package com.tokopedia.core.var;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class ContainerAds {

	public ContainerAds () {
		
	}
	
	public ArrayList<String> Name = new ArrayList<String>();
	public ArrayList<String> ImgUri = new ArrayList<String>();
	public ArrayList<String> Shop = new ArrayList<String>();
	public ArrayList<String> ShopID = new ArrayList<String>();
	public ArrayList<String> isOwner = new ArrayList<String>();
	public ArrayList<String> Var1 = new ArrayList<String>();//for catalog & product: product price, for shop: count bought
	public ArrayList<String> ID = new ArrayList<String>();
	public ArrayList<String> IsGold = new ArrayList<String>();
	public ArrayList<String> Var2 = new ArrayList<String>(); //for cata
	public ArrayList<String> Key = new ArrayList<String>();
    public ArrayList<String> CountTalk = new ArrayList<String>();
    public ArrayList<String> CountReview = new ArrayList<String>();
    public ArrayList<String> Referer = new ArrayList<String>();
    public ArrayList<String> ShopLucky = new ArrayList<String>();
    public ArrayList<String> ProductClickUrl = new ArrayList<String>();
	public ArrayList<List<Badge>> badges = new ArrayList<>();
}
