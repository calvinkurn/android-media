import React, { Component } from 'react';
import { connect } from 'react-redux';
import { ActivityIndicator, StyleSheet, TouchableOpacity, View, Image, Button, TouchableWithoutFeedback, ScrollView, TextInput, TouchableNativeFeedback, FlatList } from 'react-native';
import { emailValidation } from '../lib/utility'
import PopupDialog, { DialogTitle } from 'react-native-popup-dialog';
import { getTransactionHistory, reloadState } from '../actions/index';
import { Text } from '../common/TKPText'
import { icons } from '../lib/config'
import PopUp from '../common/TKPPopupModal'



class TransactionHistory extends Component {
  constructor(props) {
    super(props);

    this.state = {
      selectedOrder: null,
      openSelected: false
    }
  }

  componentDidMount() {
    this.props.dispatch(reloadState())
    this.props.dispatch(getTransactionHistory(0))
  }

    

  clickHandler(id){
    this.setState({ selectedOrder: id, openSelected: !this.state.openSelected })
  }



  loadMore = () => {
    this.props.dispatch(getTransactionHistory(this.props.page))
  }



  _renderTransactionHistory(rowData){
    const rowItem = rowData.item

    return (
      <View>
        <View style={styles.row}>
          <View style={styles.subRow1}>
            <Text style={styles.orderName}>{this.props.outlet_name}</Text>
            <Text style={styles.orderId}>{rowItem.order_detail.detail_invoice}</Text>
          </View>
          <View style={styles.subRow2}>
            <View style={{ width: "22%" }}>
              <Text style={[styles.font16, styles.fontbold, styles.fontcolor70]}>Waktu Transaksi</Text>
              <Text style={[styles.font16, styles.fontcolor70]}>{rowItem.order_detail.detail_order_date}</Text>
            </View>
            <View style={{ width: "40%" }}>
              <Text style={[styles.font16, styles.fontbold, styles.fontcolor70]}>Barang</Text>
              <Text style={[styles.font16, styles.fontcolor70]}>{rowItem.order_products[0].product_name.slice(0, 35)}...</Text>
            </View>
            <View style={{ width: "25%" }}>
              <Text style={[styles.font16, styles.fontbold, styles.fontcolor70]}>Total Pembayaran</Text>
              <Text style={[styles.font16, styles.fontcolor70]}>{rowItem.order_payment.payment_komisi}</Text>
            </View>
          </View>
          
          <View style={styles.subRow3}>
            <View>
              <Text style={[styles.font16, styles.fontbold, styles.fontcolor70]}>Status</Text>
              <Text style={[styles.font16, styles.fontcolor70]}>{rowItem.order_history[0].history_buyer_status}</Text>
            </View>
            <View style={{ marginTop: 25, flexDirection: 'row' }}>
              {this.state.selectedOrder !== rowData.index && !this.state.openSelected &&
                <TouchableOpacity onPress={() => this.clickHandler(rowData.index)}>
                  <View style={{ flexDirection: 'row' }}>
                    <Text style={[styles.font15, { color: '#42b549' }]}>Lihat Details</Text>
                    <Image source={{ uri: icons.arrow_down }} style={{ height: 12, width: 12, resizeMode: 'contain', marginTop: 5, marginLeft: 7 }} />
                  </View>
                </TouchableOpacity>
              }
              {this.state.selectedOrder !== rowData.index && this.state.openSelected &&
                <TouchableOpacity onPress={() => this.clickHandler(rowData.index)}>
                  <View style={{ flexDirection: 'row' }}>
                    <Text style={[styles.font15, { color: '#42b549' }]}>Lihat Details</Text>
                    <Image source={{ uri: icons.arrow_down }} style={{ height: 12, width: 12, resizeMode: 'contain', marginTop: 5, marginLeft: 7 }} />
                  </View>
                </TouchableOpacity>
              }
              {this.state.selectedOrder == rowData.index && this.state.openSelected &&
                <TouchableOpacity onPress={() => this.clickHandler(rowData.index)}>
                  <View style={{ flexDirection: 'row' }}>
                    <Text style={[styles.font15, { color: '#42b549' }]}>Sembunyikan Details</Text>
                    <Image source={{ uri: icons.arrow_up }} style={{ height: 12, width: 12, resizeMode: 'contain', marginTop: 5, marginLeft: 7 }} />
                  </View>
                </TouchableOpacity>
              }
              {this.state.selectedOrder == rowData.index && !this.state.openSelected &&
                <TouchableOpacity onPress={() => this.clickHandler(rowData.index)}>
                  <View style={{ flexDirection: 'row' }}>
                    <Text style={[styles.font15, { color: '#42b549' }]}>Lihat Details</Text>
                    <Image source={{ uri: icons.arrow_down }} style={{ height: 12, width: 12, resizeMode: 'contain', marginTop: 5, marginLeft: 7 }} />
                  </View>
                </TouchableOpacity>
              }
            </View>
          </View>


          {this.state.selectedOrder === rowData.index && this.state.openSelected &&  
            <View>
              <View style={[styles.subRow4, { marginTop: 20 }]}>
                {rowItem.order_products.map((res, idx) => 
                  <View key={idx}
                    style={{ flex: 1, flexDirection: 'row', justifyContent: 'space-between', marginTop: '1%' }}>
                    <View style={{ width: "15%", height: '20%' }}>
                      <Image source={{ uri: res.product_picture }} style={styles.productImage} ></Image>
                    </View>
                    <View style={{ width: '70%', height: '20%', flexDirection: 'column', justifyContent: 'flex-start' }}>
                      <Text style={[styles.font16, styles.fontcolor70, { width: '80%' }]}>{res.product_name} </Text>
                      <Text style={[styles.font16, styles.fontcolor70, { marginTop: 10 }]}>Jumlah Barang: {res.product_quantity}</Text>
                    </View>
                    <Text style={[styles.font16, styles.fontcolor70]}>{res.product_price}</Text>
                  </View>
                )}
              </View>
              
              <View style={styles.subRow5}>
                <View style={{ flexDirection: 'column' }}>
                  {/* <Text style={[styles.font13, styles.fontcolor61]}>Metode Pembayaran</Text>
                  <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                    <Image source={{ uri: icons.bca }} style={{ height: 24, width: 48, resizeMode: 'contain' }} />
                    <Text style={[styles.font15, styles.fontcolor54, { marginLeft: 15 }]}>BCA</Text>
                  </View> */}
                </View>
                <View style={{ flexDirection: 'row' }}>
                  <Text style={[styles.font16, styles.fontbold, styles.fontcolor70]}>Total Pembayaran: </Text>
                  <Text style={[styles.font16, styles.fontcolor70]}> {rowItem.order_payment.payment_komisi}</Text>
                </View>
              </View>
            </View>
          }
          

        </View>
      </View>
    )
  }



  toggleScreen = (visible) => {
    this.setState({ showPopUp: visible })
  }



  render() {
    return (
      <View style={styles.pageContainers}>
          <View style={[styles.containers]}>
            {this.props.data_history.length !== 0 &&
              <FlatList
                keyExtractor={(item, index) => index}
                data={this.props.data_history}
                extraData={this.state}
                onEndReached={this.loadMore}
                onEndReachedThreshold={0.5}
                renderItem={this._renderTransactionHistory.bind(this)} />
            }
            {this.props.isFetching && 
              <View style={{ marginTop:20, marginBottom:20, alignItems:'center' }}>
                <ActivityIndicator size="large" />
              </View>
            }
          </View>
        <PopupDialog
          dialogTitle={ <View style={{ flexDirection: 'row', justifyContent: 'flex-end', padding: 10 }} /> }
          dialogStyle={{ borderRadius: 10 }}
          width={504}
          height={232}
          ref={(popupDialog) => { this.popupDialog = popupDialog; }}>
          <View style={{ padding: 30, paddingTop: 0, flex: 1, flexDirection: 'column', justifyContent: 'space-between', alignItems: 'center' }}>
            <Text style={{ fontSize: 20, color: "#000000b3" }}>Bukti Pembayaran</Text>
            <View style={{ alignItems: 'center' }}>
              <Text style={{ fontSize: 14, color: "#000000b3" }}>
                Anda yakin akan membatalkan transaksi
              </Text>
              <Text style={{ fontSize: 14, color: "#000000b3" }}>
                IVR/20170609/XVII/VI/13461163
              </Text>
            </View>
            <View style={{ flexDirection: 'row' }}>
              <TouchableNativeFeedback onPress={() => { this.popupDialog.dismiss() }}>
                <View style={[styles.popupButton, { backgroundColor: "#FFFFFF", borderColor: "#e0e0e0" }]}>
                  <Text style={[styles.buttonText, { color: "#0000008a" }]}> Tidak </Text>
                </View>
              </TouchableNativeFeedback>
              <TouchableNativeFeedback onPress={() => this.popupDialog.dismiss()}>
                <View style={[styles.popupButton, { marginLeft: 30 }]}>
                  <Text style={styles.buttonText}> Ya </Text>
                </View>
              </TouchableNativeFeedback>
            </View>
          </View>
        </PopupDialog>
      </View>
    )
  }


  static navigationOptions = {
    header: null
  }
}


const styles = StyleSheet.create({
  pageContainers: {
    flex: 1,
    marginLeft: 32,
    marginRight: 32
  },
  containers: {
    flex: 1,
    alignItems: 'stretch',
  },
  row: {
    backgroundColor: '#FFFFFF',
    borderRadius: 6,
    flex: 1,
    flexDirection: 'column',
    marginTop: 32
  },
  subRow1: {
    borderBottomWidth: 1,
    borderColor: '#F0F0F0',
    flexDirection: 'row',
    padding: "3%",
    justifyContent: 'space-between'
  },
  subRow2: {
    flexDirection: 'row',
    padding: "3%",
    justifyContent: 'space-between'
  },
  subRow3: {
    borderBottomWidth: 1,
    borderColor: '#F0F0F0',
    flexDirection: 'row',
    padding: "3%",
    justifyContent: 'space-between'
  },
  subRow4: {
    borderBottomWidth: 1,
    borderColor: '#F0F0F0',
    flexDirection: 'column',
    justifyContent: 'flex-start',
    alignItems: 'flex-start',
    padding: "3%",
    paddingTop: 0
  },
  subRow5: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    padding: "3%",
  },
  subRow6: {
    borderBottomWidth: 1,
    borderColor: '#F0F0F0',
    flexDirection: 'row',
    padding: "3%",
    justifyContent: 'space-between'
  },
  productImage: {
    borderRadius: 3,
    width: 62,
    height: 62,
    resizeMode: 'cover'
  },
  button: {
    height: 32,
    width: 132,
    backgroundColor: '#42b549',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 3,
    borderWidth: 3,
    borderColor: '#41B548',
  },
  buttonText: {
    color: '#FFFFFF',
    fontSize: 11,
  },
  popupButton: {
    height: 40,
    width: 211,
    backgroundColor: '#42b549',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 3,
    borderWidth: 3,
    borderColor: '#42b549',
  },
  orderName: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#000000b3'
  },
  orderId: {
    fontSize: 20,
    color: '#000000b3'
  },
  font20: {
    fontSize: 20
  },
  font13: {
    fontSize: 13
  },
  fontcolor61: {
    color: "#00000061"
  },
  font15: {
    fontSize: 15
  },
  font14: {
    fontSize: 14
  },
  font16: {
    fontSize: 16
  },
  fontcolor54: {
    color: "#0000008a"
  },
  fontcolor70: {
    color: "#000000b3"
  },
  font22: {
    fontSize: 22
  },
  fontbold: {
    fontWeight: 'bold'
  },
  grey: {
    color: '#C8C8C8',
  }
});

const mapStateToProps = state => {
  return {
    ...state.transactionHistory,
    outlet_name: state.historyTransaction.outlet_name,
    data_history: state.historyTransaction.items,
    page: state.historyTransaction.page,
    isFetching: state.historyTransaction.isFetching,
  }
}

export default connect(mapStateToProps)(TransactionHistory)