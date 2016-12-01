package com.tokopedia.core.prototype;

import android.app.Activity;
import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;

import java.util.ArrayList;

public class ManageProductCache{
	
	private ArrayList<String> _ProdName = new ArrayList<String>();
	private ArrayList<String> _ProdID = new ArrayList<String>();
	private ArrayList<String> _ProdImgUri = new ArrayList<String>(); 
	private ArrayList<String> _DepName = new ArrayList<String>(); 
	private ArrayList<String> _EtalaseLoc = new ArrayList<String>(); 
	private ArrayList<String> _PStatus = new ArrayList<String>();
	private ArrayList<String> _Price = new ArrayList<String>();
	private ArrayList<Integer> _CurrencyCode = new ArrayList<Integer>();
	private ArrayList<Boolean> _ContainWholesale = new ArrayList<Boolean>();
    private ArrayList<Integer> _Returnable = new ArrayList<Integer>();
	private int _IsProdManager;
	private long createTime = 0;
	private int _Page = 1;
//	public static ManageProductCache singleton = new ManageProductCache();
	private Activity context;
	private LocalCacheHandler cache;
	
	public ManageProductCache(Activity context){
		this.context = context;//TODO Not done
		cache = new LocalCacheHandler(context, "CACHE_MANAGE_PRODUCT");
		if(cache.getLong("createtime", 0) !=  0){
			_ProdName.addAll(cache.getArrayListString("prodname"));
			_ProdID.addAll(cache.getArrayListString("prodid"));
			_ProdImgUri.addAll(cache.getArrayListString("prodimg"));
			_DepName.addAll(cache.getArrayListString("depname"));
			_EtalaseLoc.addAll(cache.getArrayListString("etalase"));
			_PStatus.addAll(cache.getArrayListString("pstatus"));
			_Price.addAll(cache.getArrayListString("price"));
			_ContainWholesale.addAll(cache.getArrayListBoolean("wholesale"));
			_CurrencyCode.addAll(cache.getArrayListInteger("currency"));
            _Returnable.addAll(cache.getArrayListInteger("returnable"));
			_IsProdManager = cache.getInt("ismanager");
			createTime = cache.getLong("createtime");
			_Page = cache.getInt("page");
		}
	}

	public void ClearCache(){
		_IsProdManager = 0;
		_ProdName.clear();
		_ProdID.clear();
		_ProdImgUri.clear(); 
		_DepName.clear(); 
		_EtalaseLoc.clear(); 
		_PStatus.clear();
		_Price.clear();
		_CurrencyCode.clear();
		_ContainWholesale.clear();
        _Returnable.clear();
		_Page = 1;
		LocalCacheHandler.clearCache(context, "CACHE_MANAGE_PRODUCT");
	}
	
	public static void ClearCache(Context context){
		LocalCacheHandler.clearCache(context, "CACHE_MANAGE_PRODUCT");
	}
}