---
title: "Shop Showcase"
labels:
- shop
- android
- tokopedia
- showcase
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| Contributors | [CALVYN JULIAN](https://tokopedia.atlassian.net/wiki/people/5d1adf49cdf26a0d349c76ec?ref=confluence) |
| Product Manager<br/> |  [Joshua Ghibran](https://tokopedia.atlassian.net/wiki/people/70121:7d12fd85-be0a-4d0c-a14e-8279fe20ff69?ref=confluence)  |
| Team | [Minion Ken](https://tokopedia.atlassian.net/people/team/0ac7bdd0-19b2-4196-8711-b1a0a4b07178) |
| Module type | <!--start status:YELLOW-->FEATURE<!--end status--> |
| Module Location | `features/merchant/shop_showcase` |

## Table of Contents

<!--toc-->

## Overview

This module contains showcase journey for **buyer** and **seller**, for buyer they can explore entire shop showcases, while for seller they can fully manage their showcases

## Project Description

Since there are 2 showcase journey on this module, each of journey can be describe as below:  
**Buyer Journey

:**

1. See showcases list of a shop that they visit

**Seller Journey:**

1. Add/edit my products showcase from the shop page and shop settings page.
2. Edit the name and select/deselect products that are placed inside my showcase during the add/edit showcase experience.
3. I can access my showcases easier when I want to add/edit it.
4. Re-arrange in real time my showcase list.
5. Place a product in multiple showcases.
6. Delete showcase even if there are product inside it.

## Tech Stack

- Dagger
- mvvm
- mockk
- GQL

## Flow Diagram

- **Buyer Journey**  
`ShopPageShowcaseFragment.kt`  


![](http://docs-android.tokopedia.net/images/docs/features/myflow.png)
- **Seller Journey**


	- **New showcase**  
	Create new showcase functionality  
	`ShopShowcaseAddFragment.kt` , `ShopShowcaseProductAddFragment.kt`  
	
	
	![](http://docs-android.tokopedia.net/images/docs/features/create-showcase-new-flow.png)
	- **Edit showcase**  
	Edit showcase data functionality  
	`ShopShowcaseAddFragment.kt` , `ShopShowcaseProductAddFragment.kt`  
	
	
	![](http://docs-android.tokopedia.net/images/docs/features/update-showcase-flow.png)
	- **Delete showcase**  
	Delete showcase functionality  
	`ShopShowcaseListFragment.kt`  
	
	
	![](http://docs-android.tokopedia.net/images/docs/features/delete-showcase-flow.png)
	- **Re-arrange showcase**  
	Re-arrange showcase order functionality  
	`ShopShowcaseListReorderFragment.kt`  
	
	
	![](http://docs-android.tokopedia.net/images/docs/features/reorder-showcase-flow.png)

## Navigation

- **Buyer Journey**  


![](http://docs-android.tokopedia.net/images/docs/features/etalase-buyer.gif)
- **Seller Journey**  
Seller’s facing page where users can fully manage their showcase, there are 2 entry points :   


![](http://docs-android.tokopedia.net/images/docs/features/etalase-sellerapp.gif)

![](http://docs-android.tokopedia.net/images/docs/features/etalase-buyer2.gif)


	- **New showcase**  
	Create new showcase functionality  
	`ShopShowcaseAddFragment.kt` , `ShopShowcaseProductAddFragment.kt`  
	
	
	![](http://docs-android.tokopedia.net/images/docs/features/etalase-create.gif)
	- **Edit showcase**  
	Edit showcase data functionality  
	`ShopShowcaseAddFragment.kt` , `ShopShowcaseProductAddFragment.kt`  
	
	
	![](http://docs-android.tokopedia.net/images/docs/features/etalase-update.gif)
	- **Delete showcase**  
	Delete showcase functionality  
	`ShopShowcaseListFragment.kt`  
	
	
	![](http://docs-android.tokopedia.net/images/docs/features/etalase-delete.gif)
	- **Re-arrange showcase**   
	order functionality  
	`ShopShowcaseListReorderFragment.kt`  
	
	
	![](http://docs-android.tokopedia.net/images/docs/features/etalase-reorder.gif)

## How to Use

This following steps is required to access showcase for its each types : 

- **Showcase List Buyers View**  
This type is use to explore all shop showcase for buyer  


![](http://docs-android.tokopedia.net/images/docs/features/image-20210623-092007.png)

**Setup routing**



```
val showcaseIntent = RouteManager.getIntent(context, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST)
val bundle = Bundle()
bundle.putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_DEFAULT, true)
bundle.putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_ZERO_PRODUCT, false)
bundle.putString(ShopShowcaseParamConstant.EXTRA_SHOP_ID, shopId)
bundle.putString(ShopShowcaseParamConstant.EXTRA_SHOP_TYPE, "regular/gold_merchant/official_store")

showcaseIntent.putExtra(ShopShowcaseParamConstant.EXTRA_BUNDLE, bundle)
// please use startActivityForResult to handle selected showcase and redirect it
// to showcase detail page
startActivityForResult(showcaseIntent, YOUR_REQ_CODE)
```

**Handle on activity result**



```
// handle if user click on specific showcase to see the detail
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    when (requestCode) {
        YOUR_REQUEST_CODE_FOR_SHOWCASE -> {
          val selectedShowcaseId = data?.getStringExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_ID)
          val showcaseDetailIntent = RouteManager.getIntent(
              context, 
              ApplinkConstInternalMarketplace.SHOP_PAGE_PRODUCT_LIST, 
              shopId, 
              selectedShowcaseId
          )
          showcaseDetailIntent.putExtra(EXTRA_IS_NEED_TO_RELOAD_DATA_SHOP_PRODUCT_LIST, true)
          startActivity(showcaseDetailIntent)
        }
    }
}
```
- **Showcase Sellers View**  
as for sellers there are 3 action type for showcases, each type is use for different use cases : 


	- **Showcase List Seller Management**  
	This type is use to manage all shop showcases  
	
	
	![](http://docs-android.tokopedia.net/images/docs/features/image-20210623-085003.png)
	
	**Setup routing**
	
	
	
	```
	val showcaseListIntent = RouteManager.getIntent(context, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST)
	val bundle = Bundle()
	bundle.putString(ShopShowcaseParamConstant.EXTRA_SHOP_ID, userSession.shopId) // seller shop id
	bundle.putString(ShopShowcaseParamConstant.EXTRA_SELECTED_ETALASE_ID, "")
	bundle.putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_DEFAULT, true)
	bundle.putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_ZERO_PRODUCT, true)
	
	showcaseListIntent.putExtra(ShopShowcaseParamConstant.EXTRA_BUNDLE, showcaseListBundle)
	startActivityForResult(showcaseListIntent, YOUR_REQ_CODE_SHOWCASE)
	```
	
	**Handle on activity result :** 
	
	
	
	```
	// handle if user click on specific showcase to see the detail
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
	    super.onActivityResult(requestCode, resultCode, data)
	    when (requestCode) {
	        YOUR_REQ_CODE_SHOWCASE -> {
	          val shopId = userSession.shopId // seller shop id
	          val selectedShowcaseId = data.getStringExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_ID)
	          val showcaseDetailIntent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.SHOP_PAGE_PRODUCT_LIST, shopId, selectedShowcaseId)
	          showcaseDetailIntent.putExtra(EXTRA_IS_NEED_TO_RELOAD_DATA_SHOP_PRODUCT_LIST, true)
	          startActivity(showcaseDetailIntent)
	        }
	    }
	}
	```
	- **Multi showcase picker**  
	This type is use to assign selected product to multi showcase at once, currently implemented on `Add Edit Product Page`   
	
	
	![](http://docs-android.tokopedia.net/images/docs/features/image-20210623-073729.png)
	
	**Setup routing**
	
	
	
	```
	val showcaseMultiPickerIntent = RouteManager.getIntent(context, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST)
	val bundle = Bundle()
	bundle.putString(ShopShowcaseParamConstant.EXTRA_IS_NEED_TO_OPEN_SHOWCASE_PICKER, ShowcasePickerType.CHECKBOX)
	bundle.putParcelableArrayList(ShopShowcaseParamConstant.EXTRA_PRE_SELECTED_SHOWCASE_PICKER, currentSelectedShowcase) // for edit purpose
	
	showcaseMultiPickerIntent.putExtra(ShopShowcaseParamConstant.EXTRA_BUNDLE, bundle)
	startActivityForResult(showcaseMultiPickerIntent, ShopShowcaseParamConstant.SHOWCASE_PICKER_RESULT_REQUEST_CODE)
	```
	
	**Handle on activity result :** 
	
	
	
	```
	// handle to get selected showcases list
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
	    super.onActivityResult(requestCode, resultCode, data)
	    when (requestCode) {
	        SHOWCASE_PICKER_RESULT_REQUEST_CODE -> {
	          val selectedShowcaseList: ArrayList<ShowcaseItemPicker> = data.getParcelableArrayListExtra(
	            ShopShowcaseParamConstant.EXTRA_PICKER_SELECTED_SHOWCASE
	          ) ?: ArrayList()
	          
	          // do something with selected showcases...
	        }
	    }
	}
	```
	- **Single Showcase Picker**  
	This type is use to move selected product to another showcase, currently implemented on `Product Manage Page`
	
	![](http://docs-android.tokopedia.net/images/docs/features/image-20210623-080355.png)
	
	**Setup Routing**
	
	
	
	```
	val showcaseSinglePickerIntent = RouteManager.getIntent(this, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST)
	val bundle = Bundle()
	bundle.putString(ShopShowcaseParamConstant.EXTRA_IS_NEED_TO_OPEN_SHOWCASE_PICKER, ShowcasePickerType.RADIO)
	
	showcaseSinglePickerIntent.putExtra(ShopShowcaseParamConstant.EXTRA_BUNDLE, bundle)
	startActivityForResult(intent, ShopShowcaseParamConstant.SHOWCASE_PICKER_RESULT_REQUEST_CODE)
	```
	
	**Handle on activity result :** 
	
	
	
	```
	// handle to get selected showcase
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
	    super.onActivityResult(requestCode, resultCode, data)
	    when (requestCode) {
	        SHOWCASE_PICKER_RESULT_REQUEST_CODE -> {
	          val selectedShowcase: ShowcaseItemPicker = data.getParcelableExtra(
	            ShopShowcaseParamConstant.EXTRA_PICKER_SELECTED_SHOWCASE
	          ) ?: ShowcaseItemPicker()
	          
	          // do something with selected showcase...
	        }
	    }
	}
	```

## GQL



|  | **Gql Doc** |
| --- | --- |
| **Get featured showcase list** | [Get Featured Showcase (Etalase Unggulan)](https://tokopedia.atlassian.net/wiki/spaces/MC/pages/738857426/Shop+Showcase+GQL#ShopFeaturedShowcase)  |
| **Get regular showcase list (buyer view)** | [Get Non-Featured Showcase (Regular Etalase)](https://tokopedia.atlassian.net/wiki/spaces/MC/pages/738857426/Shop+Showcase+GQL#ShopShowcasesByShopID-(Buyer-View-%2F-Shop-Page))  |
| **Get all showcase list (seller view)** | [Get All Showcase - (Seller Only)](https://tokopedia.atlassian.net/wiki/spaces/MC/pages/738857426/Shop+Showcase+GQL#ShopShowcases-(Seller-View))  |
| **Create, edit, delete and reorder showcase** | [Manage Showcase (Create, Edit, Delete, Reorder)](#)  |

## Useful Links



| **Docs Type** | **URL** |
| --- | --- |
| PRD | <https://docs.google.com/document/d/1IVxZS-6kNfAiW9vCWivcx7ugX-Hbw7qjIs2kXcCzIi4/edit> [- **Connect your Google account**](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/980509792/Shop+Showcase#) |
| Figma | [Figma Showcase Seller View](https://www.figma.com/file/zZCj5olHST8HdIOvSyECkq/M-Add-Etalase?node-id=1532%3A116031)[Figma Showcase Buyer View](https://www.figma.com/file/kAK21BJ1l05XI0xrS5gXdQ/%5BM%5D---Etalase-Revamp-Buyer-View?node-id=1222%3A0)[Figma Multi Showcase Picker](https://www.figma.com/file/UhdaoUSE51lfLcWF6Vy2ku/%5BM%5D---Add-%26-Edit-Product?node-id=6248%3A1)[Figma Single Showcase Picker](https://www.figma.com/file/zZCj5olHST8HdIOvSyECkq/%5BM%5D---Add-Etalase?node-id=1092%3A85514) |
| Data Layer | <https://docs.google.com/spreadsheets/d/1zRn4uPs_HBi7Eb8wPuiXt3rj1HnJveMkzOaOg0z2EAo/edit#gid=306885993>  |

