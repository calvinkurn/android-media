---
title: "Image Picker Insta Common"
labels:
- feed
- ccp
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status-->  |
| --- | --- |
| Contributors | [Meyta Taliti](https://tokopedia.atlassian.net/wiki/people/5c8f676b8c3aae2d15113a7c?ref=confluence) [Jonathan Darwin](https://tokopedia.atlassian.net/wiki/people/60d02446a01e11006ae4c8f0?ref=confluence) [Fachrizal Achmad Zulfikar Mursalin](https://tokopedia.atlassian.net/wiki/people/62a6713a192edb006f9d9a2b?ref=confluence)  |
| Team | [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) |
| Release date | 15 Oct 2021 / <!--start status:BLUE-->MA-3.147<!--end status--> |
| Module type | <!--start status:PURPLE-->INTERNAL-LIBRARY<!--end status--> |
| Module Location | `libraries.image_picker_insta_common` | `libraries/image_picker_insta_common` |

## Table of Contents

<!--toc-->

## Release Notes

<!--start expand:15 Oct 21 - MA-3.147 / SA-2.077-->
Released  
<https://github.com/tokopedia/android-tokopedia-core/pull/22050>
<!--end expand-->

## Overview

**Image Picker Insta Common** is a module to support [Image Picker Insta](/wiki/spaces/PA/pages/2139065547/Image+Picker+Insta) module. Actually we don’t have much classes here since most of them are already located within Insta Picker Insta and I don’t see any benefit of putting the code here. So, if in the future you need to add some classes for Image Picker Insta, you can consider to put it directly in Image Picker Insta instead of this module.

### Project Description

Image Picker Insta Common has 3 main helper classes:

1. **Tracker Provider**  
Somehow the tracker is attached from outside (from the source page that opens Image Picker Insta). The tracker is provided through this **TrackerProvider** class.
2. **Menu Manager**  
A helper class to add menu in Action Bar.
3. **Query Builder**  
This class is a helper class used to generate an applink with the given queries.

## How-to

1. **Tracker Provider**


	1. Create an tracker implementation of `TrackerContract`
	
	
	
	```
	class MyImagePickerInstaTracker : TrackerContract {
	
	    override fun onNextButtonClick() {
	
	    }
	
	    override fun onBackButtonFromPicker() {
	
	    }
	
	    override fun onCameraButtonFromPickerClick() {
	
	    }
	
	    override fun onRecordButtonClick(mediaType: String) {
	
	    }
	}
	```
	2. Attach the tracker after navigating to Image Picker Insta Page
	
	
	
	```
	class MyActivity : AppCompatActivity() {
	
	  @Inject
	  lateinit var myImagePickerInstaTracker: TrackerContract
	
	  fun openImagePickerInstaPage() {
	    ...
	    startActivity(intent)
	    
	    // Attach your tracker here
	    TrackerProvider.attachTracker(myImagePickerInstaTracker)
	  }
	}
	```
	3. **(optional)** you can remove the tracker if it’s not needed anymore
	
	
	
	```
	fun clear() {
	  TrackerProvider.removeTracker()
	}
	```
2. **Menu Manager**



```
override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
  MenuManager.addCustomMenu(
    activity = activity,
    menuTitle = "Lanjut",
    isActive = true,
    menu = menu
  ) {
    // execute your code here when the menu is clicked
  }
}
```
3. **Query Builder**



```
fun generateImagePickerInstaQuery(): String {
  val queries = listOf<Pair<String, Any>>(
      Pair(BundleData.TITLE, BundleData.VALUE_POST_SEBAGAI),
      Pair(BundleData.APPLINK_AFTER_CAMERA_CAPTURE, ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2),
      Pair(BundleData.APPLINK_FOR_GALLERY_PROCEED, ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2),
      Pair(BundleData.MAX_MULTI_SELECT_ALLOWED, BundleData.VALUE_MAX_MULTI_SELECT_ALLOWED),
      Pair(BundleData.KEY_IS_OPEN_FROM, BundleData.VALUE_IS_OPEN_FROM_SHOP_PAGE),
  )
  
  return ImagePickerInstaQueryBuilder.generateQuery(queries)
}

fun openImagePickerInstaPage() {
  val appLink = RouteManager.route("${ApplinkConst.IMAGE_PICKER_V2}?${generateImagePickerInstaQuery()}")
}
```



---

## FAQ

<!--start expand:When to use Query Builder?-->
When you need to open Image Picker Insta Page with Applink. If you are able to open it with intent, then you don’t need to use this class.
<!--end expand-->

