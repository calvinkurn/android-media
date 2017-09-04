import React, { Component } from 'react'
import { connect } from 'react-redux'
import {
  Button,
  StyleSheet,
  AsyncStorage,
  TouchableNativeFeedback,
  TouchableOpacity,
  Platform,
  Text,
  View,
  Image,
} from 'react-native'
import { addToWishlist, removeFromWishlist } from '../../actions/actions'
import { NavigationModule } from 'NativeModules'
import { icons } from '../../../../icons/index'

// // Icon from Firebase
// const icon_love = 'https://firebasestorage.googleapis.com/v0/b/tokopedia-android.appspot.com/o/icon-wishlist-red.png?alt=media&token=7cb838f9-3f3b-4705-8218-eb242a0377f1'
// const icon_notlove = 'https://firebasestorage.googleapis.com/v0/b/tokopedia-android.appspot.com/o/icon-wishlist.png?alt=media&token=f13280d1-7d29-4e3c-838e-f040fa8a50c2'


class Wishlist extends Component {
  _onTap = (isWishlist, pId) => {
    AsyncStorage.getItem('user_id').then(res => { 
      const User_ID = res

      if (User_ID != null){
        if (isWishlist) {
          this.props.dispatch(removeFromWishlist(pId, User_ID))
        } else {
          this.props.dispatch(addToWishlist(pId, User_ID))
        }
      } else {
        NavigationModule.navigateToLoginWithResult().then(response => { console.log(response) })
      }
    })
  }

  render() {
    const isWishlist = this.props.isWishlist || false
    const { productId } = this.props
    const Touchable = Platform.OS === 'android' ? TouchableNativeFeedback : TouchableOpacity
    return (
      <View style={styles.wrapper}>
        <Touchable onPress={() => this._onTap(isWishlist, productId)}>
           <View>
            {
              isWishlist ? (<Image source={ icons.icon_wishlist_red } style={{width:20, height:20, margin:10}} />) :
                (<Image source={ icons.icon_wishlist } style={{width:20, height:20, margin:10}} />)
            }
          </View> 
        </Touchable>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  wrapper: {
    position: 'absolute',
    right: 0,
    top: 0,
    paddingTop: 2,
    paddingRight: 0,
    paddingBottom: 1,
    paddingLeft: 2,
    backgroundColor: '#fff',
    borderWidth: 0,
    width: 35,
    height: 35,
    borderRadius: 20,
    elevation: 4,
    justifyContent: 'center',
    alignItems: 'center'
  }
})

export default connect()(Wishlist)