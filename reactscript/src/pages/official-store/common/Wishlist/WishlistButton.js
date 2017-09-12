import React, { Component } from 'react'
import { connect } from 'react-redux'
import {
  Button,
  StyleSheet,
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


class Wishlist extends Component {
  _onTap = (isWishlist, pId) => {
    NavigationModule.getCurrentUserId().then(uuid => {
        if (uuid != ''){
          if (isWishlist) {
            this.props.dispatch(removeFromWishlist(pId, uuid))
          } else {
            this.props.dispatch(addToWishlist(pId, uuid))
          }
        } else {
          NavigationModule.navigateToLoginWithResult()
        }
      })
  }

  render() {
    const isWishlist = this.props.isWishlist || false
    const { productId } = this.props
    // const Touchable = Platform.OS === 'android' ? TouchableNativeFeedback : TouchableOpacity
    return (
      <View style={styles.wrapper}>
        <TouchableOpacity onPress={() => this._onTap(isWishlist, productId)}>
           <View>
            {
              isWishlist ? (<Image source={ icons.icon_wishlist_red } style={{width:20, height:20, margin:10}} />) :
                (<Image source={ icons.icon_wishlist } style={{width:20, height:20, margin:10}} />)
            }
          </View> 
        </TouchableOpacity>
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