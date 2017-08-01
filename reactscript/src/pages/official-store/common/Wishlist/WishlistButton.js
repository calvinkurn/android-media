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
import { icons } from '../../../../components/icons'
import { addToWishlist, removeFromWishlist } from '../../actions/actions'

class Wishlist extends Component {
  _onTap = (isWishlist, pId, User_ID) => {
    if (isWishlist) {
      this.props.dispatch(removeFromWishlist(pId, User_ID))
    } else {
      this.props.dispatch(addToWishlist(pId, User_ID))
    }
  }

  render() {
    const isWishlist = this.props.isWishlist || false
    const { productId, User_ID } = this.props
    const Touchable = Platform.OS === 'android' ? TouchableNativeFeedback : TouchableOpacity
    return (
      <View style={styles.wrapper}>
        <Touchable onPress={() => this._onTap(isWishlist, productId, User_ID)}>
           <View>
            {
              isWishlist ? (<Image source={icons.icon_wishlist_red} style={{width:20, height:20, margin:10}} />) :
                (<Image source={icons.icon_wishlist} style={{width:20, height:20, margin:10}} />)
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