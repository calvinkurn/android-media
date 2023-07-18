---
title: "Product Detail Info BottomSheet"
labels:
- product_detail_info
- pdp
- main-app
- android
- seller
---


| **Status** |  <!--start status:PURPLE-->DEVELOPMENT<!--end status--> <!--start status:GREEN-->RELEASE<!--end status-->  |
| --- | --- |
| Contributors | [Yovi Eka Putra](https://tokopedia.atlassian.net/wiki/people/62b94558018dea0d40730d06?ref=confluence) [Kelvindo Sutan](https://tokopedia.atlassian.net/wiki/people/5ff2a8fe44065f013f93507c?ref=confluence) [Yehezkiel .](https://tokopedia.atlassian.net/wiki/people/5c94aa7a7792242c8613ad14?ref=confluence)  |
| Product Manager | [Amirul Hadi Wibowo](https://tokopedia.atlassian.net/wiki/people/60bdafb9dae567006894003a?ref=confluence)  |
| Team | [Minion Stuart](https://tokopedia.atlassian.net/people/team/eeba862a-bd9d-472c-b901-415b15b1a37e)  |
| Module type | <!--start status:BLUE-->SUB-FEATURE<!--end status-->  |
| Module Location | `features/merchant/product_detail/info` |

## Table of Contents

<!--toc-->

## Overview

Product Detail Info BottomSheet is a bottom sheet for explain more detail regarding product description.



| ![](res/image-20230717-043243.png)<br/> | ![](res/Spesifikasi%20Bottomsheet.png)<br/> |
| --- | --- |

![](res/Screenshot%202023-07-17%20at%2016.29.55.png)

### Background

Simplify product content in product detail page, so that in bottom sheet buyer can more freely to describe regarding they product.



---

## Navigation

1. Open PDP
2. Scroll to Product Detail Info
3. Click `"Baca Selengkapnya"`

## How-to

If you want to use this bottom-sheet, we have provided helper to easy to use



```
ProductDetailInfoHelper.showBottomSheetInfo(
    fragmentActivity: FragmentActivity,
    daggerComponent: ProductDetailComponent?,
    listener: ProductDetailBottomSheetListener,
    p1Data: DynamicProductInfoP1?,
    sizeChartImageUrl: String?,
    infoData: ProductDetailInfoDataModel,
    forceRefresh: Boolean,
    isOpenSpecification: Boolean,
    annotationExtParam: String = ""
)
```



| **Param Name** | **Description** |
| --- | --- |
| `fragmentActivity` | your activity |
| `daggerComponent` | dagger of product detail component |
| `listener` | interface between bottom-sheet to PDP. |
| `sizeCartImageUrl` | image url for guideline image from variant data |
| `infoData` | Data model to annotation type especially if `showBottomSheet` is `true`  |
| `forceRefresh` | if PDP to swipe refresh, so this field should to `true` |
| `isOpenSpecification` | this field will differentiate bottom-sheet item type. |
| `annotationExtParam` | this value from PDP data, usually we will passing to info bottom sheet graphQL, needed BE. |



---

## Tech Stack

Product detail info bottom sheet have a recycler-view to display multiple type item:



| **TYPE** | **SCREENSHOT** | **DATA SOURCE** |
| --- | --- | --- |
| Header | ![](res/Screenshot%202023-07-18%20at%2013.30.58.png)<br/> | `PdpGetDetailBottomSheet.bottomsheetData.componentName='header'` |
| Annotation | ![](res/Screenshot%202023-07-18%20at%2013.36.42.png)<br/> | `PdpGetDetailBottomSheet.bottomsheetData.componentName='detail'`but this data get from`infoData: ProductDetailInfoDataModel` |
| Discussion | ![](res/Screenshot%202023-07-18%20at%2013.28.24.png)<br/> | `PdpGetDetailBottomSheet.discussion` |
| Expandable List | ![](res/Screenshot%202023-07-18%20at%2013.35.36.png)<br/> | `PdpGetDetailBottomSheet.dataShopNotes` |
| Card | ![](res/Screenshot%202023-07-18%20at%2013.27.01.png)<br/> | `PdpGetDetailBottomSheet.bottomSheetData.componentName='custom_info'` |
| Catalog | ![](res/Screenshot%202023-07-18%20at%2013.27.59.png)<br/> | `PdpGetDetailBottomSheet.bottomSheetData.componentName='catalog'` |
| Expandable Image | ![](res/Screenshot%202023-07-18%20at%2013.35.01.png)<br/> | `PdpGetDetailBottomSheet.bottomSheetData.componentName='panduan_ukuran'` |
| Shimmering for product catalog | ![](res/Screenshot%202023-07-18%20at%2013.32.25.png)<br/> | `ProductInfoParcelData.isOpenSpecification = true` |
| Shimmering for product description only | ![](res/Screenshot%202023-07-18%20at%2013.31.25.png)<br/> | `ProductInfoParcelData.isOpenSpecification == false && isCatalog == true` |
| Legacy Shimmering | ![](res/Screenshot%202023-07-18%20at%2013.33.27.png)<br/> | `otherwise this` |

## Data Source



| **Request** | **Response** |
| --- | --- |
| <br/><br/>```<br/>query PdpGetDetailBottomSheet($productId:String,$shopId:String,$catalogId:String, $isGiftable:Boolean, $parentId:String, $bottomsheetParam:String){<br/>  pdpGetDetailBottomSheet(productID:$productId, shopID:$shopId, catalogID:$catalogId, isGiftable:$isGiftable, parentID:$parentId, bottomsheetParam:$bottomsheetParam){<br/>    bottomsheetData{<br/>      title<br/>      componentName<br/>      isApplink<br/>      isShowable<br/>      value<br/>      applink<br/>      icon<br/>      row {<br/>        key<br/>        value<br/>      }<br/>    }<br/>    dataShopNotes{<br/>      shopNotesData{<br/>        shopNotesID<br/>        title<br/>        content<br/>        isTerms<br/>        position<br/>        url<br/>        updateTime<br/>        updateTimeUTC<br/>      }<br/>      error<br/>    }<br/>    discussion{<br/>      title<br/>      buttonType<br/>      buttonCopy<br/>    }<br/>    error{<br/>      Code<br/>      Message<br/>      DevMessage<br/>    }<br/>  }<br/>}<br/>```<br/><br/> | <br/><br/>```<br/>[<br/>    {<br/>        "data": {<br/>            "pdpGetDetailBottomSheet": {<br/>                "bottomsheetData": [<br/>                    {<br/>                        "applink": "",<br/>                        "componentName": "header",<br/>                        "icon": "",<br/>                        "isApplink": false,<br/>                        "isShowable": true,<br/>                        "row": [],<br/>                        "title": "",<br/>                        "value": ""<br/>                    },<br/>                    {<br/>                        "applink": "",<br/>                        "componentName": "detail",<br/>                        "icon": "",<br/>                        "isApplink": false,<br/>                        "isShowable": true,<br/>                        "row": [],<br/>                        "title": "Detail produk",<br/>                        "value": ""<br/>                    },<br/>                    {<br/>                        "applink": "",<br/>                        "componentName": "deskripsi",<br/>                        "icon": "",<br/>                        "isApplink": true,<br/>                        "isShowable": true,<br/>                        "row": [],<br/>                        "title": "Deskripsi produk",<br/>                        "value": ""<br/>                    },<br/>                    {<br/>                        "applink": "",<br/>                        "componentName": "panduan_ukuran",<br/>                        "icon": "",<br/>                        "isApplink": true,<br/>                        "isShowable": false,<br/>                        "row": [],<br/>                        "title": "Panduan ukuran",<br/>                        "value": ""<br/>                    },<br/>                    {<br/>                        "applink": "",<br/>                        "componentName": "panduan_perawatan",<br/>                        "icon": "",<br/>                        "isApplink": true,<br/>                        "isShowable": false,<br/>                        "row": [],<br/>                        "title": "Panduan perawatan",<br/>                        "value": ""<br/>                    },<br/>                    {<br/>                        "applink": "",<br/>                        "componentName": "informasi_penting",<br/>                        "icon": "",<br/>                        "isApplink": true,<br/>                        "isShowable": false,<br/>                        "row": [],<br/>                        "title": "Informasi penting",<br/>                        "value": ""<br/>                    }<br/>                ],<br/>                "dataShopNotes": {<br/>                    "error": "",<br/>                    "shopNotesData": []<br/>                },<br/>                "discussion": {<br/>                    "buttonCopy": "",<br/>                    "buttonType": "",<br/>                    "title": "Ada hal lain yang ingin ditanyakan?"<br/>                },<br/>                "error": {<br/>                    "Code": 0,<br/>                    "DevMessage": "",<br/>                    "Message": ""<br/>                }<br/>            }<br/>        }<br/>    }<br/>]<br/>```<br/><br/> |

## Useful Links

- [Figma](https://www.figma.com/file/oN7uy5TBohqGmuZX1eFnIq/Informasi-Produk%2C-Catalog%2C-Annotation?type=design&node-id=5831-171597&mode=design&t=jumuJNOTEvnm2UVc-0)

