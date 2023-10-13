---
title: "Product Photo Adapter"
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| Contributors | [YOHANN Prananta (Unlicensed)](https://tokopedia.atlassian.net/wiki/people/5de4eab04ae7b80d0d19f990?ref=confluence) [Faisal Ramdani](https://tokopedia.atlassian.net/wiki/people/5def016f73b0bd0e4d82fa6e?ref=confluence)  |
| Team | [Minion Ken](https://tokopedia.atlassian.net/people/team/0ac7bdd0-19b2-4196-8711-b1a0a4b07178) |
| Release date | 2020 |
| Module type |  <!--start status:GREEN-->LIBRARY<!--end status--> |
| Module Location |  `features/merchant/product_photo_adapter` |

## Table of Contents

<!--toc-->

## Overview

Product photo adapter is library made from RecyclerView Adapter that able to display and swapping position between images.

### Project Overview

For now, library is only used at Product Add Edit module, but actually it can be used to another module that has same purposes. Here is the features:

1. Able to add or remove the image.
2. Scroll to see all preview of each image.
3. The image can be rearranged by holding tap and swipe between preview image.

![](res/Screen%20Shot%202023-02-02%20at%2017.56.50.png)

## Flow Diagram

This diagram shows this library works.

![](res/Untitled%20Diagram.png)

## How-to

1. Add Dependency


	- ```
	implementation projectOrAar(rootProject.ext.libraries.productPhotoAdapter)
	```
2. Declare as recycler view adapter


	- ```
	val productPhotoAdapter = ProductPhotoAdapter(
	  [maxSize],
	  [usePlaceholder],
	  [productPhotoPaths],
	  [onPhotoChangeListener]
	)
	```
	- `maxSize: Int` → Maximum picture count that can be added into adapter
	
	`usePlaceholder: Boolean` → Toggle display placeholder when no data added
	
	`productPhotoPaths: MutableList<String>` → List of string of images path 
	
	`onPhotoChangeListener: OnPhotoChangeListener` → Listener interface for adapter actions. here is the actions:
	
	
		- `onStartDrag`
		- `onRemovePhoto`
3. Set as recycler view adapter


	- ```
	yourRecyclerView.adapter = productPhotoAdapter
	```
4. Register touch helper


	- ```
	val photoItemTouchHelperCallback = PhotoItemTouchHelperCallback(yourRecyclerView)
	photoItemTouchHelper = ItemTouchHelper(photoItemTouchHelperCallback)
	photoItemTouchHelper?.attachToRecyclerView(yourRecyclerView)
	```



---

## Action Items

This module has action item that can be visit here:

<https://docs.google.com/document/d/1853gzFyPQ_86b7L1Iho6epPd5jWscCfBRB20LPavBSA/edit#heading=h.rhtinuvosc6z> 

## Useful Links

- <https://www.figma.com/file/UhdaoUSE51lfLcWF6Vy2ku/%5BM%5D---Add-%26-Edit-Product?node-id=1439%3A63204&t=FQzS5idrADE8iCVy-4>
