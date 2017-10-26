import React, { Component } from 'react';
import { connect } from 'react-redux';
import { StyleSheet, View, Image, Button, TouchableWithoutFeedback, ScrollView, TextInput, TouchableNativeFeedback, FlatList } from 'react-native';
import { emailValidation } from '../lib/utility'
import PopupDialog, { DialogTitle } from 'react-native-popup-dialog';
import { getTransactionHistory } from '../actions/index';
import { Text } from '../common/TKPText'
import { icons } from '../lib/config'
import PopUp from '../common/TKPPopupModal'



class TransactionHistory extends Component {

  constructor(props) {

    super(props);

    this.state = {
      selectedOrder: ""
    };
  }

  componentDidMount() {
    this.props.dispatch(getTransactionHistory());
  }

  _toggleProduct(id) {
    this.setState({
      selectedOrder: id
    });
  }

  _renderProductList(rowData) {

    return (
      <View style={{ flex: 1, flexDirection: 'row', justifyContent: 'space-between', marginTop: '1%' }}>
        <View style={{ width: "15%", height: '20%' }}>
          <Image source={{ uri: rowData.item.imageUrl }} style={styles.productImage} ></Image>
        </View>
        <View style={{ width: '70%', height: '20%', flexDirection: 'column', justifyContent: 'flex-start' }}>
          <Text style={[styles.font14, styles.fontcolor71, { width: '80%' }]}>{rowData.item.name} </Text>
          <Text style={[styles.font13, styles.fontcolor61, { marginTop: 10 }]}>Jumlah Barang: {rowData.item.qty}</Text>
        </View>
        <Text style={[styles.font14, styles.fontcolor71]}>{rowData.item.price}</Text>
      </View>
    );
  }
  _renderTarnsactionHistory(rowData) {
   const rowItem = rowData.item;
  const products = rowItem.products;
    return (
      <View style={styles.row}>
        <View style={styles.subRow1}>
          <Text style={styles.orderName}>{rowItem.orderName}</Text>
          <Text style={styles.orderId}>{rowItem.orderId}</Text>
        </View>

        <View style={styles.subRow2}>
          <View style={{ width: "22%" }}>
            <Text style={[styles.font13, styles.fontcolor61]}>Waktu Transaksi</Text>
            <Text style={[styles.font15, styles.fontcolor54]}>{rowItem.time}</Text>
          </View>
          <View style={{ width: "40%" }}>
            <Text style={[styles.font13, styles.fontcolor61]}>Barang</Text>
            <Text style={[styles.font15, styles.fontcolor54]}>{rowItem.products[0].name.slice(0, 35)}...</Text>
          </View>
          <View style={{ width: "25%" }}>
            <Text style={[styles.font13, styles.fontcolor61]}>Total Pembayaran</Text>
            <Text style={[styles.font15, styles.fontcolor70]}>{rowItem.totalPrice}</Text>
          </View>

        </View>

        <View style={styles.subRow3}>
          <View>
            <Text style={[styles.font13, styles.fontcolor61]}>Status</Text>
            <Text style={[styles.font15, styles.fontcolor54]}>Status {rowItem.status}</Text>
          </View>
          <View style={{ marginTop: 25, flexDirection: 'row' }}>
            <TouchableNativeFeedback
              onPress={() =>
                this._toggleProduct(this.state.selectedOrder != rowItem.orderId ? rowItem.orderId : "")
              }
            >
              <Text style={[styles.font15, { color: '#42b549' }]}> {(this.state.selectedOrder != rowItem.orderId) ? "Lihat Details" : "Sembunyikan"}</Text>
            </TouchableNativeFeedback>
            <TouchableNativeFeedback
              onPress={() =>
                this._toggleProduct(this.state.selectedOrder != rowItem.orderId ? rowItem.orderId : "")
              }
            >
              {(this.state.selectedOrder != rowItem.orderId) ?
                <Image source={{ uri: icons.arrow_down }} style={{ height: 12, width: 12, resizeMode: 'cover', marginTop: 5, marginLeft: 7 }} />
                :
                <Image source={{ uri: icons.arrow_up }} style={{ height: 12, width: 12, resizeMode: 'cover', marginTop: 5, marginLeft: 7 }} />
              }
            </TouchableNativeFeedback>
          </View>

        </View>
        {(this.state.selectedOrder == rowItem.orderId) ?
             <View style={[styles.subRow4]}>
                  <FlatList
                       keyExtractor={(item, index) => item.id}
                       data={products}
                       renderItem={this._renderProductList.bind(this)} />
               </View>
          : null
        }

        {(this.state.selectedOrder == rowItem.orderId) ?
          <View style={styles.subRow5} >
            <View style={{ flexDirection: 'column' }}>
              <Text style={[styles.font13, styles.fontcolor61]}>Metode Pembayaran</Text>
              <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                <Image source={{ uri: icons.bca }} style={{ height: 24, width: 48, resizeMode: 'cover' }} >
                </Image>
                <Text style={[styles.font15, styles.fontcolor54]}>BCA</Text>
              </View>
            </View>
            <View style={{ flexDirection: 'row' }}>
              <Text style={[styles.font16, styles.fontcolor70]}>Total Pembayaran: </Text>
              <Text style={[styles.font16, styles.fontcolor70]}> {rowItem.totalPrice}</Text>
            </View>
          </View>
          : null}

        {(this.state.selectedOrder == rowItem.orderId) ?
          <View style={styles.subRow6} >
            <Text style={[styles.font13, styles.fontcolor61]}> {(rowItem.isCompleted) ? "Transaksi sudah melewati batas waktu untuk dibatalkan" : "Transaki dapat dibatalkan sebelum jam 19.00"}</Text>
            {(!rowItem.isCompleted) ?
              <TouchableNativeFeedback onPress={() => { this.popupDialog.show() }}>
                <View style={[styles.button]}>
                  <Text style={styles.buttonText}> Batalkan Transaksi </Text>
                </View>
              </TouchableNativeFeedback>
              :
              <TouchableNativeFeedback>
                <View style={[styles.button, { backgroundColor: '#E0E0E0', borderColor: '#E0E0E0' }]}>
                  <Text style={[styles.buttonText, { color: '#0000001A' }]}> Batalkan Transaksi </Text>
                </View>
              </TouchableNativeFeedback>
            }
          </View>
          : null}
      </View>
    );
  }

  toggleScreen = (visible) => {
    this.setState({ showPopUp: visible })
  }

  render() {
    return (
      <View style={{ flex: 1 }}>
        {/* <View style={{
          height: 55,
          backgroundColor: '#42b549',
          flexDirection: 'row',
          alignItems: 'center',
          justifyContent: 'space-around'
        }}>
          <View style={{ width: '5%', left: 10 }}>
          </View>
          <View style={{ width: '90%' }}>
            <Text style={{ fontSize: 20, color: '#fff' }}>Riwayat Transaksi</Text>
          </View>
        </View> */}

        <ScrollView>
            <View style={[styles.containers]}>
                 <FlatList
                      keyExtractor={(item, index) => item.orderId}
                      data={this.props.items}
                      extraData={this.state}
                      renderItem={this._renderTarnsactionHistory.bind(this)} />
              </View>
        </ScrollView>
        <PopupDialog
          dialogTitle={
            <View style={{ flexDirection: 'row', justifyContent: 'flex-end', padding: 10 }}>
            </View>
          }
          dialogStyle={{ borderRadius: 10 }}
          width={504}
          height={232}
          ref={(popupDialog) => { this.popupDialog = popupDialog; }}>
          <View style={{ padding: 30, paddingTop: 0, flex: 1, flexDirection: 'column', justifyContent: 'space-between', alignItems: 'center' }}>
            <Text style={{ fontSize: 20, color: "#000000b3" }}>
              Bukti Pembayaran
                </Text>
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
              <TouchableNativeFeedback onPress={() => {
                this.popupDialog.dismiss()
              }}>
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
  };

}


const styles = StyleSheet.create({
  containers: {
    flex: 1,
    alignItems: 'stretch',
    margin: 20,
    marginTop: 0
  },
  row: {
    backgroundColor: '#FFFFFF',
    borderRadius: 6,
    flex: 1,
    flexDirection: 'column',
    marginTop: 30
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
    fontSize: 15,
    color: '#000000b3'
  },
  orderId: {
    fontSize: 13,
    color: '#00000061'
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
    ...state.transactionHistory
  }
}

export default connect(mapStateToProps)(TransactionHistory)