---
title: Proof of Delivery (POD)
labels:
- proof-of-delivery
- logistic
- android
---

<!--left header table-->
| Status               | <!--start status:GREEN-->RELEASE<!--end status-->                                                                                                                                                                                                                                                           |
|----------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Contributors         | [Irpan .](https://tokopedia.atlassian.net/wiki/people/6253578a3bf0f0007015669c?ref=confluence) [Fakhira Devina](https://tokopedia.atlassian.net/wiki/people/61077e53b704b40068e80a8e?ref=confluence) [Eka Desyantoro](https://tokopedia.atlassian.net/wiki/people/6283196bd9ddcc006e9c7a85?ref=confluence)  |
| Product Manager<br/> | [Aulia Rasyid](https://tokopedia.atlassian.net/wiki/people/613e9e61e7c328007069f2b6?ref=confluence)                                                                                                                                                                                                         |
| Team<br/>            | [Minion Bob](https://tokopedia.atlassian.net/people/team/2373d8a6-1afc-4f2a-aa7a-63855c273051)                                                                                                                                                                                                              |
| Module type          | <!--start status:YELLOW-->FEATURE<!--end status-->                                                                                                                                                                                                                                                          |
| Product PRD          | [Show PoD after order finished](https://tokopedia.atlassian.net/wiki/spaces/LG/pages/1925155114/Show+PoD+after+order+finished)                                                                                                                                                                              |
| Module Location      | `features/logistic/logisticorder`                                                                                                                                                                                                                                                                           |

<!--toc-->

## Release Notes

<!--start expand:24 June (Ma-3.180)-->
[Ticket](https://tokopedia.atlassian.net/browse/AN-37123)
<!--end expand-->

## **Overview**

### Background

To increase shipping transparency to Buyer, Seller and Tokopedia Nakama OPS team about the Pickup and delivery flow by courier, we want to show the Proof of Pickup and Proof of Delivery on Lacak Page

### Project Description

 Proof of Delivery is an image captured by the courier to confirm the pick-up. The image captured by the courier can be seen through the lacak page on BOM and SOM page.

### UI Page

![](../res/pod/view%20POD.png)

## Navigation

- `tokopedia://shipping/pod/{order_id}?image_id={image_id}&description={description}`

## How-to

### **Gradle**

`implementation projectOrAar(rootProject.ext.features.logisticCommon)`

### **Call POD Activity**

To use POD its need to image Id, Description image, and Order ID. here’s an example how to call pod.



```
 val appLink = Uri.parse(ApplinkConstInternalLogistic.PROOF_OF_DELIVERY).buildUpon()
            .appendQueryParameter(PodConstant.QUERY_IMAGE_ID, {imageId})
            .appendQueryParameter(PodConstant.QUERY_DESCRIPTION, {description})
            .build()
            .toString()
val intent = RouteManager.getIntent(activity, appLink, {orderId})
startActivity(intent)
```

Variable

- `imageId : String`
- `Description : String`
- `orderId : String`

Constant Key 

- `ApplinkConstInternalLogistic.PROOF_OF_DELIVERY`
- `PodConstant.QUERY_IMAGE_ID`
- `PodConstant.QUERY_DESCRIPTION`

### Generate Image url

 If necessary to show image pod, feel free to generate image url by using 

`LogisticImageDeliveryHelper.getDeliveryImage(params)`

Parameter :


- `imageId: String` ***[Mandatory]***
- `orderId: Long` ***[Mandatory]***
- `size: String` ***[Mandatory]***  
can use :  
`LogisticImageDeliveryHelper.IMAGE_SMALL_SIZE` for small size  
or `LogisticImageDeliveryHelper.IMAGE_LARGE_SIZE` for large size
- `userId: String`***[Mandatory]***  
can get from `userSession`
- `osType: Int` ***[Mandatory]***  
os Type. for default can use `LogisticImageDeliveryHelper.DEFAULT_OS_TYPE`
- `deviceId: String` ***[Mandatory]***  
can be get from `userSession`

### Show Pod image

since load image Pod need to include accessToken. here is extension can be use to display pod image.

`loadImagePod` . extended from ImageView.



```
imgPod.loadImagePod(
        context,
        accessToken,
        url,
        imagePlaceholder,
        imageError
)
```

- imgPod : ImageView

Parameter :

- `context : Context` ***[Mandatory]***
- `accessToken: String` => ***[Mandatory]***  
can be get from `userSession`
- `url : Url` ***[Mandatory]***
- `imagePlaceholder : Drawable?` ***[Optional]***
- `imageError : Drawable?` ***[Optional]***



---

## Useful Links

- [Figma](https://www.figma.com/file/kgIIxg830Nm2LCbUXvuZ3t/POD-after-finish-order?node-id=149%3A11017)

 

 