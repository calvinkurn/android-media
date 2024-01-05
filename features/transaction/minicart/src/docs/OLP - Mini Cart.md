---
title: "OLP - Mini Cart"
labels:
- minicart
- purchase-platform
- android
- cart
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status-->  |
| --- | --- |
| Contributors | [Ilham .](https://tokopedia.atlassian.net/wiki/people/5de4d2148743750d00b7cc12?ref=confluence) [Felicia Widjaja](https://tokopedia.atlassian.net/wiki/people/5c90c19d196abe2e884c82ed?ref=confluence)  |
| Product Manager | [Nevin Jonaputra Liauw](https://tokopedia.atlassian.net/wiki/people/60863032ccda29006b45df13?ref=confluence)  |
| Team | [Minion Bob](https://tokopedia.atlassian.net/people/team/2373d8a6-1afc-4f2a-aa7a-63855c273051) |
| Release date | `MA-3.239` |
| Module type | <!--start status:BLUE-->SUB-FEATURE<!--end status-->  |
| Module Location | Module : `features.transaction.minicart` Package : `features/transaction/minicart/src/main/java/com/tokopedia/minicart/bmgm` |
| PRD | <https://docs.google.com/document/d/1nJ6kRnjo_6g93GA0CSpZkZOkazgypBJVNfYt4Sjf3_8/>  |

## Table of Contents

<!--toc-->

## Overview

### Project Description

This is a Mini Cart Widget for *Buy More Get/Save More(BMGM or BMSM)* project which is used in the Offer Landing Page(OLP). Like any other cart, this widget will show the list of products already attached by user. There are two main components in the Mini Cart : 

1. **Mini Cart View**

![](https://docs-android.tokopedia.net/images/docs/main/Screen%20Shot%202023-09-05%20at%2010.40.48-20230905-034115.png)
2. **Mini Cart Detail BottomSheet**  
**Unique case :** 


	1. No GQL hit, but using `PersistanceCacheManager` instead. So, the entry point to this page should store the data using it, before opening this page. Please check [this section about](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2350156541/OLP+-+Mini+Cart#How-to) how to store data
	2. Show product price per item and use wholesale price if eligible  
	
	
	![](https://docs-android.tokopedia.net/images/docs/main/Screen%20Shot%202023-09-05%20at%2010.42.19-20230905-034250.png)

## Tech Stack

- MVVM
- Coroutine Flow

## Flow Diagram

There are some flow in mini cart

- **Initial Load** and **Add to Cart** flow of *Mini Cart* in *Offer Landing Page*

![](https://docs-android.tokopedia.net/images/docs/main/image-20230904-091601.png)

- View **Cart Detail Bottom Sheet** flow  
*Cart Detail Bottom Sheet* page is using local database as the data source. We are using ***PersistentCacheManager***. Here is the flow :

![](https://docs-android.tokopedia.net/images/docs/main/image-20230904-095906.png)

- Open **Cart Page/Cek Keranjang** flow  
Before we open the Cart Page, we should make all of attached product in offer landing page automatically checked in Cart Page by invoking ***mutation setCheckboxState***. It’s fire and forget, so it will open the Cart page no matter what. Here is the detail flow :

![](https://docs-android.tokopedia.net/images/docs/main/open%20cart%20flow-20230912-062242.png)

## Navigation

### Entrypoints

1. **Mini Cart Widget** only used on Offer Landing Page(OLP)
2. **Mini Cart Detail Bottom Sheet** entry point :


	1. From ***Mini Cart*** on Offer Landing Page
	
	![](https://docs-android.tokopedia.net/images/docs/main/Screen%20Shot%202023-09-05%20at%2010.35.55-20230905-033635.png)
	2. Click ***(i)*** icon on Checkout Page, in ***Buy More Save More******Section***
	
	![](https://docs-android.tokopedia.net/images/docs/main/Screen%20Shot%202023-10-06%20at%2016.51.35-20231006-095151.png)

### GQL

- **Mini Cart**  
For mini cart GQL query, please refer this page. <https://tokopedia.atlassian.net/wiki/spaces/TTD/pages/1929479868/External+Mini+Cart+V3>  
To know more detail about implemented GQL query, please check it in this class `features/transaction/minicart/src/main/java/com/tokopedia/minicart/bmgm/domain/usecase/GetBmgmMiniCartDataUseCase.kt`
- **Submit set check list state**  
Mutation : 



```
mutation setCheckboxState($params: [CartCheckboxStateParam]) {
  set_cartlist_checkbox_state(params: $params) {
    status
    error_message
    data {
      success
    }
  }
}
```

Param : 



```
{
  "params": [
    {
      "cart_id": "",
      "checkbox_state": true
    }
  ]
}
```

## How-to

**How to use Mini Cart Detail Bottom Sheet?**

1. Save the required data using `PersistanceCacheManager` and using data class `BmgmCommonDataModel`



```
...
fun saveCartDataToLocalStorage() {
    val data = BmgmCommonDataModel(...)
    PersistentCacheManager.instance.put(BmgmCommonDataModel.PARAM_KEY_BMGM_DATA, data)
}
...
```
2. Route to the Cart Detail Bottom Sheet, using appLink : `tokopedia-android-internal://global/bmgm-mini-cart`



```
...
fun openMiniCartBottomSheet() {
    saveCartDataToLocalStorage()
    RouteManager.route(context, ApplinkConstInternalGlobal.BMGM_MINI_CART)
}
...
```
3. Get the data and show it on the bottom sheet



```
fun getMiniCartData(): BmgmCommonDataModel {
    val commonData = PersistentCacheManager.instance.get<BmgmCommonDataModel>(
        BmgmCommonDataModel.PARAM_KEY_BMGM_DATA, BmgmCommonDataModel::class.java, null
    )
    return commonData ?: throw RuntimeException("No cart data stored in local cache")
}
```

## Action Items

- Implement diffUtil on OLP Mini Cart for better performance

## Useful Links

- [Figma](https://www.figma.com/file/KfiaRWebt5C9r5o5BnwbJQ/Buy-More-Get-More?type=design&node-id=4671-407937&mode=dev)
- [Timeline Mastersheet](https://docs.google.com/spreadsheets/d/1E6pBhbAV-u-JuQBWRvH_gCDICVjn1mJhlXBlVXi44l8/edit#gid=190299628)
- Tracker


	- [MyNakama 1](https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4231)
	- [MyNakama 2](https://mynakama.tokopedia.com/datatracker/requestdetail/view/4164)
